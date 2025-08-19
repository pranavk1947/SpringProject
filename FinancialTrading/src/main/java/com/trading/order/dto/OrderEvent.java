package com.trading.order.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String orderId;
    private String userId;
    private String symbol;
    private String eventType;
    private OrderResponse order;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
}