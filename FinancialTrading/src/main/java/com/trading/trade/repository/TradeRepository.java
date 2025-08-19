package com.trading.trade.repository;

import com.trading.trade.Trade;
import com.trading.trade.TradeStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TradeRepository extends R2dbcRepository<Trade, Long> {
    
    // Find trades by user (as buyer or seller)
    @Query("""
        SELECT * FROM trades 
        WHERE buyer_user_id = :userId OR seller_user_id = :userId
        ORDER BY created_at DESC
        """)
    Flux<Trade> findTradesByUser(@Param("userId") String userId);
    
    // Find trades by symbol
    Flux<Trade> findBySymbolOrderByCreatedAtDesc(String symbol);
    
    // Find trades by order ID
    @Query("SELECT * FROM trades WHERE buyer_order_id = :orderId OR seller_order_id = :orderId")
    Flux<Trade> findTradesByOrderId(@Param("orderId") String orderId);
    
    // Find recent trades for price discovery
    @Query("""
        SELECT * FROM trades 
        WHERE symbol = :symbol 
        AND created_at >= :since
        ORDER BY created_at DESC
        LIMIT :limit
        """)
    Flux<Trade> findRecentTrades(
            @Param("symbol") String symbol,
            @Param("since") LocalDateTime since,
            @Param("limit") int limit);
    
    // Get latest trade price for a symbol
    @Query("SELECT * FROM trades WHERE symbol = :symbol ORDER BY created_at DESC LIMIT 1")
    Mono<Trade> findLatestTradeBySymbol(@Param("symbol") String symbol);
    
    // Volume aggregation queries
    @Query("""
        SELECT COALESCE(SUM(quantity), 0) as volume
        FROM trades 
        WHERE symbol = :symbol 
        AND created_at >= :since
        AND status = 'SETTLED'
        """)
    Mono<BigDecimal> getTradingVolume(
            @Param("symbol") String symbol,
            @Param("since") LocalDateTime since);
    
    // VWAP calculation
    @Query("""
        SELECT COALESCE(SUM(quantity * price) / NULLIF(SUM(quantity), 0), 0) as vwap
        FROM trades 
        WHERE symbol = :symbol 
        AND created_at >= :since
        AND status = 'SETTLED'
        """)
    Mono<BigDecimal> getVolumeWeightedAveragePrice(
            @Param("symbol") String symbol,
            @Param("since") LocalDateTime since);
    
    // Find pending trades for settlement
    Flux<Trade> findByStatusOrderByCreatedAt(TradeStatus status);
    
    // Trading statistics
    @Query("""
        SELECT 
            COUNT(*) as trade_count,
            COALESCE(SUM(quantity), 0) as total_volume,
            COALESCE(MIN(price), 0) as min_price,
            COALESCE(MAX(price), 0) as max_price,
            COALESCE(AVG(price), 0) as avg_price
        FROM trades 
        WHERE symbol = :symbol 
        AND created_at >= :since
        AND status = 'SETTLED'
        """)
    Mono<TradingStatistics> getTradingStatistics(
            @Param("symbol") String symbol,
            @Param("since") LocalDateTime since);
}