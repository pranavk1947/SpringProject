package com.trading.trade;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import com.trading.order.OrderSide;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "tradeId")
@ToString
@Table("trades")
public class Trade {
    
    @Id
    private Long id;
    
    @Column("trade_id")
    @NotNull
    @Size(min = 1, max = 50)
    private String tradeId;
    
    @NotNull
    @Size(min = 1, max = 10)
    private String symbol;
    
    @Column("buyer_order_id")
    @NotNull
    private String buyerOrderId;
    
    @Column("seller_order_id")
    @NotNull
    private String sellerOrderId;
    
    @Column("buyer_user_id")
    @NotNull
    private String buyerUserId;
    
    @Column("seller_user_id")
    @NotNull
    private String sellerUserId;
    
    @NotNull
    @Positive
    private BigDecimal quantity;
    
    @NotNull
    @Positive
    private BigDecimal price;
    
    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    
    @Column("settlement_date")
    private LocalDateTime settlementDate;
    
    @Builder.Default
    private TradeStatus status = TradeStatus.PENDING;
    
    // Business methods
    public BigDecimal getTotalValue() {
        return quantity.multiply(price);
    }
    
    public void settle() {
        this.status = TradeStatus.SETTLED;
        this.settlementDate = LocalDateTime.now();
    }
    
    public void fail(String reason) {
        this.status = TradeStatus.FAILED;
    }
    
    public boolean isPending() {
        return status == TradeStatus.PENDING;
    }
    
    public boolean isSettled() {
        return status == TradeStatus.SETTLED;
    }
}