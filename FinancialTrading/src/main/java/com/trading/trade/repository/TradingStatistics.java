package com.trading.trade.repository;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * Trading statistics aggregation result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingStatistics {
    private Long tradeCount;
    private BigDecimal totalVolume;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal avgPrice;
}