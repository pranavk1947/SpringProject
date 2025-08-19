package com.trading.order.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
    
    @Positive(message = "Quantity must be positive")
    @DecimalMin(value = "0.00000001", message = "Quantity must be at least 0.00000001")
    private BigDecimal quantity;
    
    @DecimalMin(value = "0.00000001", message = "Price must be positive")
    private BigDecimal price;
    
    @DecimalMin(value = "0.00000001", message = "Stop price must be positive")
    private BigDecimal stopPrice;
}