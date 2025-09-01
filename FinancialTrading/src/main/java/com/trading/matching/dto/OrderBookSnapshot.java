package com.trading.matching.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.trading.order.repository.OrderBookLevel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookSnapshot {
    private String symbol;
    private List<OrderBookLevel> bids;
    private List<OrderBookLevel> asks;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
    
    public BigDecimal getBestBidPrice() {
        return bids != null && !bids.isEmpty() ? 
                bids.get(0).getPrice() : BigDecimal.ZERO;
    }
    
    public BigDecimal getBestAskPrice() {
        return asks != null && !asks.isEmpty() ? 
                asks.get(0).getPrice() : BigDecimal.ZERO;
    }
    
    public BigDecimal getSpread() {
        BigDecimal bestBid = getBestBidPrice();
        BigDecimal bestAsk = getBestAskPrice();
        
        if (bestBid.compareTo(BigDecimal.ZERO) > 0 && bestAsk.compareTo(BigDecimal.ZERO) > 0) {
            return bestAsk.subtract(bestBid);
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalBidVolume() {
        return bids != null ? 
                bids.stream()
                    .map(OrderBookLevel::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add) 
                : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalAskVolume() {
        return asks != null ? 
                asks.stream()
                    .map(OrderBookLevel::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add) 
                : BigDecimal.ZERO;
    }
}