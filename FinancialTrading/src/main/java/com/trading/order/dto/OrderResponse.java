package com.trading.order.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.trading.order.OrderSide;
import com.trading.order.OrderType;
import com.trading.order.OrderStatus;
import com.trading.order.TimeInForce;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private String orderId;
    private String symbol;
    private OrderSide side;
    private OrderType type;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal stopPrice;
    private BigDecimal filledQuantity;
    private BigDecimal averagePrice;
    private OrderStatus status;
    private TimeInForce timeInForce;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime expiresAt;
    
    // Computed fields
    public BigDecimal getRemainingQuantity() {
        return quantity.subtract(filledQuantity);
    }
    
    public boolean isFullyFilled() {
        return filledQuantity.compareTo(quantity) == 0;
    }
    
    public boolean isPartiallyFilled() {
        return filledQuantity.compareTo(BigDecimal.ZERO) > 0 && 
               filledQuantity.compareTo(quantity) < 0;
    }
}