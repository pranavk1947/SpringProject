package com.trading.order.repository;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * Represents aggregated order book level data for market depth queries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookLevel {
    private BigDecimal price;
    private BigDecimal totalQuantity;
    private Long orderCount;
}