package com.trading.order;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "orderId")
@ToString(exclude = {"id"})
@Table("orders")
// Index definitions will be handled by database migration scripts
public class Order {
    
    @Id
    private Long id;
    
    @Column("order_id")
    @NotNull
    @Size(min = 1, max = 50)
    private String orderId;
    
    @Column("user_id")
    @NotNull
    @Size(min = 1, max = 50)
    private String userId;
    
    @NotNull
    @Size(min = 1, max = 10)
    private String symbol;
    
    @NotNull
    private OrderSide side;
    
    @NotNull
    private OrderType type;
    
    @NotNull
    @Positive
    private BigDecimal quantity;
    
    private BigDecimal price;
    
    @Column("stop_price")
    private BigDecimal stopPrice;
    
    @Column("filled_quantity")
    @Builder.Default
    private BigDecimal filledQuantity = BigDecimal.ZERO;
    
    @Column("average_price")
    @Builder.Default
    private BigDecimal averagePrice = BigDecimal.ZERO;
    
    @NotNull
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column("time_in_force")
    @Builder.Default
    private TimeInForce timeInForce = TimeInForce.GTC;
    
    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @Column("expires_at")
    private LocalDateTime expiresAt;
    
    // Business logic methods
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
    
    public boolean isActive() {
        return status == OrderStatus.OPEN || status == OrderStatus.PARTIALLY_FILLED;
    }
    
    public void fill(BigDecimal filledQty, BigDecimal fillPrice) {
        if (filledQty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Filled quantity must be positive");
        }
        
        BigDecimal newFilledQuantity = this.filledQuantity.add(filledQty);
        if (newFilledQuantity.compareTo(quantity) > 0) {
            throw new IllegalArgumentException("Cannot fill more than order quantity");
        }
        
        // Calculate new average price
        BigDecimal totalValue = this.filledQuantity.multiply(this.averagePrice)
                                   .add(filledQty.multiply(fillPrice));
        this.averagePrice = totalValue.divide(newFilledQuantity, 8, BigDecimal.ROUND_HALF_UP);
        this.filledQuantity = newFilledQuantity;
        
        // Update status
        if (isFullyFilled()) {
            this.status = OrderStatus.FILLED;
        } else {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
    }
    
    public void cancel() {
        if (!isActive()) {
            throw new IllegalStateException("Cannot cancel order in status: " + status);
        }
        this.status = OrderStatus.CANCELLED;
    }
    
    public void reject(String reason) {
        this.status = OrderStatus.REJECTED;
    }
    
    public void expire() {
        if (isActive()) {
            this.status = OrderStatus.EXPIRED;
        }
    }
}