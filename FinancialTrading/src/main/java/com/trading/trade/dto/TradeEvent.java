package com.trading.trade.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeEvent {
    private String tradeId;
    private String symbol;
    private String buyerOrderId;
    private String sellerOrderId;
    private String buyerUserId;
    private String sellerUserId;
    private BigDecimal quantity;
    private BigDecimal price;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
}