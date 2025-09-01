package com.trading.risk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.trading.order.Order;
import com.trading.trade.Trade;
import com.trading.risk.dto.RiskAssessment;
import com.trading.risk.dto.PositionRisk;
import com.trading.common.exception.RiskLimitExceededException;
import com.trading.order.repository.OrderRepository;
import com.trading.trade.repository.TradeRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskManagementService {
    
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final CircuitBreakerService circuitBreakerService;
    
    @Value("${trading.risk.max-position-size:1000000}")
    private BigDecimal maxPositionSize;
    
    @Value("${trading.risk.max-daily-loss:100000}")
    private BigDecimal maxDailyLoss;
    
    @Value("${trading.risk.circuit-breaker-threshold:0.05}")
    private BigDecimal circuitBreakerThreshold;
    
    private final ConcurrentMap<String, PositionRisk> userPositions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, BigDecimal> dailyLosses = new ConcurrentHashMap<>();
    
    public Mono<RiskAssessment> assessOrderRisk(Order order) {
        return Mono.fromCallable(() -> {
            RiskAssessment assessment = RiskAssessment.builder()
                    .orderId(order.getOrderId())
                    .userId(order.getUserId())
                    .symbol(order.getSymbol())
                    .passed(true)
                    .timestamp(java.time.Instant.now())
                    .build();
            
            // Check position size limits
            if (!checkPositionSizeLimit(order)) {
                assessment.setPasssed(false);
                assessment.addRiskViolation("Position size limit exceeded");
                log.warn("Position size limit exceeded for user: {}, order: {}", 
                        order.getUserId(), order.getOrderId());
            }
            
            // Check daily loss limits
            if (!checkDailyLossLimit(order.getUserId())) {
                assessment.setPasssed(false);
                assessment.addRiskViolation("Daily loss limit exceeded");
                log.warn("Daily loss limit exceeded for user: {}", order.getUserId());
            }
            
            // Check circuit breaker
            if (circuitBreakerService.isTripped(order.getSymbol())) {
                assessment.setPasssed(false);
                assessment.addRiskViolation("Circuit breaker is active for symbol: " + order.getSymbol());
                log.warn("Circuit breaker active for symbol: {}", order.getSymbol());
            }
            
            return assessment;
        })
        .doOnSuccess(assessment -> {
            if (!assessment.isPassed()) {
                log.info("Risk assessment failed for order: {} - violations: {}", 
                        order.getOrderId(), assessment.getRiskViolations());
            }
        });
    }
    
    public Mono<Void> updatePositionRisk(Trade trade) {
        return Mono.fromRunnable(() -> {
            updateUserPosition(trade.getBuyerUserId(), trade.getSymbol(), 
                    trade.getQuantity(), trade.getPrice());
            updateUserPosition(trade.getSellerUserId(), trade.getSymbol(), 
                    trade.getQuantity().negate(), trade.getPrice());
            
            // Update daily P&L
            updateDailyPnL(trade.getBuyerUserId(), trade);
            updateDailyPnL(trade.getSellerUserId(), trade);
            
            // Check for circuit breaker triggers
            checkCircuitBreakerTriggers(trade);
        })
        .doOnSuccess(v -> log.debug("Updated position risk for trade: {}", trade.getTradeId()));
    }
    
    public Mono<PositionRisk> getUserPositionRisk(String userId, String symbol) {
        return Mono.fromCallable(() -> {
            String key = userId + ":" + symbol;
            return userPositions.getOrDefault(key, PositionRisk.builder()
                    .userId(userId)
                    .symbol(symbol)
                    .position(BigDecimal.ZERO)
                    .averagePrice(BigDecimal.ZERO)
                    .unrealizedPnL(BigDecimal.ZERO)
                    .build());
        });
    }
    
    public Mono<BigDecimal> getUserDailyLoss(String userId) {
        return Mono.fromCallable(() -> dailyLosses.getOrDefault(userId, BigDecimal.ZERO));
    }
    
    public void resetDailyLimits() {
        dailyLosses.clear();
        log.info("Daily risk limits reset");
    }
    
    private boolean checkPositionSizeLimit(Order order) {
        String key = order.getUserId() + ":" + order.getSymbol();
        PositionRisk currentPosition = userPositions.getOrDefault(key, 
                PositionRisk.builder()
                        .position(BigDecimal.ZERO)
                        .build());
        
        BigDecimal newPosition = order.getSide() == com.trading.order.OrderSide.BUY ?
                currentPosition.getPosition().add(order.getQuantity()) :
                currentPosition.getPosition().subtract(order.getQuantity());
        
        return newPosition.abs().compareTo(maxPositionSize) <= 0;
    }
    
    private boolean checkDailyLossLimit(String userId) {
        BigDecimal currentLoss = dailyLosses.getOrDefault(userId, BigDecimal.ZERO);
        return currentLoss.compareTo(maxDailyLoss) < 0;
    }
    
    private void updateUserPosition(String userId, String symbol, BigDecimal quantity, BigDecimal price) {
        String key = userId + ":" + symbol;
        userPositions.compute(key, (k, existingPosition) -> {
            if (existingPosition == null) {
                return PositionRisk.builder()
                        .userId(userId)
                        .symbol(symbol)
                        .position(quantity)
                        .averagePrice(price)
                        .lastUpdateTime(LocalDateTime.now())
                        .build();
            }
            
            BigDecimal newPosition = existingPosition.getPosition().add(quantity);
            BigDecimal newAveragePrice = calculateNewAveragePrice(
                    existingPosition.getPosition(), existingPosition.getAveragePrice(),
                    quantity, price);
            
            return existingPosition.toBuilder()
                    .position(newPosition)
                    .averagePrice(newAveragePrice)
                    .lastUpdateTime(LocalDateTime.now())
                    .build();
        });
    }
    
    private void updateDailyPnL(String userId, Trade trade) {
        // Simplified P&L calculation - in practice, would need mark-to-market
        BigDecimal tradePnL = BigDecimal.ZERO; // Calculate based on trade vs average cost
        
        if (tradePnL.compareTo(BigDecimal.ZERO) < 0) {
            dailyLosses.merge(userId, tradePnL.abs(), BigDecimal::add);
        }
    }
    
    private void checkCircuitBreakerTriggers(Trade trade) {
        // Check if price movement exceeds threshold
        tradeRepository.findLatestTradeBySymbol(trade.getSymbol())
                .subscribe(lastTrade -> {
                    BigDecimal priceChange = trade.getPrice().subtract(lastTrade.getPrice())
                            .divide(lastTrade.getPrice(), 4, java.math.RoundingMode.HALF_UP);
                    
                    if (priceChange.abs().compareTo(circuitBreakerThreshold) > 0) {
                        circuitBreakerService.tripCircuitBreaker(trade.getSymbol(), 
                                "Price movement exceeded threshold: " + priceChange);
                    }
                });
    }
    
    private BigDecimal calculateNewAveragePrice(BigDecimal currentPosition, BigDecimal currentAvgPrice,
                                              BigDecimal newQuantity, BigDecimal newPrice) {
        if (currentPosition.compareTo(BigDecimal.ZERO) == 0) {
            return newPrice;
        }
        
        BigDecimal totalValue = currentPosition.multiply(currentAvgPrice)
                .add(newQuantity.multiply(newPrice));
        BigDecimal totalQuantity = currentPosition.add(newQuantity);
        
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return totalValue.divide(totalQuantity, 4, java.math.RoundingMode.HALF_UP);
    }
}