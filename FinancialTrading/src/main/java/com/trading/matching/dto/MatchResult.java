package com.trading.matching.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.trading.order.Order;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    private String symbol;
    private Order buyOrder;
    private Order sellOrder;
    private BigDecimal quantity;
    private BigDecimal price;
    private boolean matched;
}