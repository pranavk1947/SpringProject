package com.trading.trade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.trading.trade.Trade;
import com.trading.trade.dto.TradeEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeEventPublisher {
    
    private static final String TRADE_EVENTS_TOPIC = "trade-events";
    private static final String TRADE_EXECUTIONS_TOPIC = "trade-executions";
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public Mono<Trade> publishTradeEvent(Trade trade) {
        TradeEvent event = TradeEvent.builder()
                .tradeId(trade.getTradeId())
                .symbol(trade.getSymbol())
                .buyerOrderId(trade.getBuyerOrderId())
                .sellerOrderId(trade.getSellerOrderId())
                .buyerUserId(trade.getBuyerUserId())
                .sellerUserId(trade.getSellerUserId())
                .quantity(trade.getQuantity())
                .price(trade.getPrice())
                .timestamp(java.time.Instant.now())
                .build();
                
        return Mono.fromFuture(
                kafkaTemplate.send(TRADE_EVENTS_TOPIC, trade.getSymbol(), event)
                        .completable()
        )
        .doOnSuccess(result -> log.debug("Published trade event: {} for symbol: {}", 
                trade.getTradeId(), trade.getSymbol()))
        .doOnError(error -> log.error("Failed to publish trade event for trade: {}", 
                trade.getTradeId(), error))
        .then(Mono.just(trade));
    }
    
    public Mono<Void> publishTradeExecution(Trade trade) {
        return Mono.fromFuture(
                kafkaTemplate.send(TRADE_EXECUTIONS_TOPIC, trade.getSymbol(), trade)
                        .completable()
        )
        .doOnSuccess(result -> log.debug("Published trade execution: {} for symbol: {}", 
                trade.getTradeId(), trade.getSymbol()))
        .doOnError(error -> log.error("Failed to publish trade execution for trade: {}", 
                trade.getTradeId(), error))
        .then();
    }
}