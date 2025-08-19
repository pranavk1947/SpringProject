package com.trading.order.repository;

import com.trading.order.Order;
import com.trading.order.OrderStatus;
import com.trading.order.OrderSide;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {
    
    // Find orders by user
    Flux<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    
    // Find by order ID
    Mono<Order> findByOrderId(String orderId);
    
    // Find active orders for a symbol
    @Query("SELECT * FROM orders WHERE symbol = :symbol AND status IN ('OPEN', 'PARTIALLY_FILLED') ORDER BY created_at")
    Flux<Order> findActiveOrdersBySymbol(@Param("symbol") String symbol);
    
    // Find orders by symbol and side for matching engine
    @Query("""
        SELECT * FROM orders 
        WHERE symbol = :symbol 
        AND side = :side 
        AND status IN ('OPEN', 'PARTIALLY_FILLED')
        ORDER BY CASE 
            WHEN :side = 'BUY' THEN price DESC, created_at ASC
            ELSE price ASC, created_at ASC
        END
        """)
    Flux<Order> findOrdersForMatching(@Param("symbol") String symbol, @Param("side") OrderSide side);
    
    // Find orders by price range for market depth
    @Query("""
        SELECT * FROM orders 
        WHERE symbol = :symbol 
        AND side = :side 
        AND status IN ('OPEN', 'PARTIALLY_FILLED')
        AND (:minPrice IS NULL OR price >= :minPrice)
        AND (:maxPrice IS NULL OR price <= :maxPrice)
        ORDER BY price, created_at
        """)
    Flux<Order> findOrdersByPriceRange(
            @Param("symbol") String symbol,
            @Param("side") OrderSide side,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
    
    // Find expired orders
    @Query("SELECT * FROM orders WHERE expires_at IS NOT NULL AND expires_at <= :now AND status IN ('OPEN', 'PARTIALLY_FILLED')")
    Flux<Order> findExpiredOrders(@Param("now") LocalDateTime now);
    
    // Count active orders by user
    @Query("SELECT COUNT(*) FROM orders WHERE user_id = :userId AND status IN ('OPEN', 'PARTIALLY_FILLED')")
    Mono<Long> countActiveOrdersByUser(@Param("userId") String userId);
    
    // Find orders by status
    Flux<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    
    // Find user orders by symbol
    Flux<Order> findByUserIdAndSymbolOrderByCreatedAtDesc(String userId, String symbol);
    
    // Performance optimized query for order book levels
    @Query("""
        SELECT price, SUM(quantity - filled_quantity) as total_quantity, COUNT(*) as order_count
        FROM orders 
        WHERE symbol = :symbol 
        AND side = :side 
        AND status IN ('OPEN', 'PARTIALLY_FILLED')
        GROUP BY price
        ORDER BY CASE 
            WHEN :side = 'BUY' THEN price DESC
            ELSE price ASC
        END
        LIMIT :limit
        """)
    Flux<OrderBookLevel> findOrderBookLevels(
            @Param("symbol") String symbol,
            @Param("side") OrderSide side,
            @Param("limit") int limit);
    
    // Delete old completed orders for cleanup
    @Query("DELETE FROM orders WHERE status IN ('FILLED', 'CANCELLED', 'REJECTED', 'EXPIRED') AND updated_at < :cutoffDate")
    Mono<Integer> deleteOldCompletedOrders(@Param("cutoffDate") LocalDateTime cutoffDate);
}