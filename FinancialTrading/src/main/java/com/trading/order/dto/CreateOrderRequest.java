package com.trading.order.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.trading.order.OrderSide;
import com.trading.order.OrderType;
import com.trading.order.TimeInForce;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotBlank(message = "Symbol is required")
    @Size(min = 1, max = 10, message = "Symbol must be between 1 and 10 characters")
    private String symbol;
    
    @NotNull(message = "Order side is required")
    private OrderSide side;
    
    @NotNull(message = "Order type is required")
    private OrderType type;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @DecimalMin(value = "0.00000001", message = "Quantity must be at least 0.00000001")
    private BigDecimal quantity;
    
    @DecimalMin(value = "0.00000001", message = "Price must be positive")
    private BigDecimal price;
    
    @DecimalMin(value = "0.00000001", message = "Stop price must be positive")
    private BigDecimal stopPrice;
    
    @Builder.Default
    private TimeInForce timeInForce = TimeInForce.GTC;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;
    
    // Client order ID for idempotency
    @Size(max = 50, message = "Client order ID must be less than 50 characters")
    private String clientOrderId;
}