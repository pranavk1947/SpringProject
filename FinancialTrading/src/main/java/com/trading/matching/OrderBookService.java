package com.trading.matching;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import com.trading.order.Order;
import com.trading.order.OrderSide;
import com.trading.order.repository.OrderBookLevel;
import com.trading.order.repository.OrderRepository;
import com.trading.matching.dto.OrderBookSnapshot;
import com.trading.matching.dto.MarketDepth;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBookService {
    
    private final OrderRepository orderRepository;
    private final Map<String, OrderBookSnapshot> orderBookCache = new ConcurrentHashMap<>();
    \n    public Mono<OrderBookSnapshot> getOrderBookSnapshot(String symbol, int levels) {\n        return Mono.fromCallable(() -> orderBookCache.get(symbol))\n                .switchIfEmpty(buildOrderBookSnapshot(symbol, levels))\n                .subscribeOn(Schedulers.boundedElastic());\n    }\n    \n    public Mono<MarketDepth> getMarketDepth(String symbol, int levels) {\n        Mono<List<OrderBookLevel>> bidLevels = orderRepository\n                .findOrderBookLevels(symbol, OrderSide.BUY, levels)\n                .collectList();\n                \n        Mono<List<OrderBookLevel>> askLevels = orderRepository\n                .findOrderBookLevels(symbol, OrderSide.SELL, levels)\n                .collectList();\n                \n        return Mono.zip(bidLevels, askLevels)\n                .map(tuple -> MarketDepth.builder()\n                        .symbol(symbol)\n                        .bids(tuple.getT1())\n                        .asks(tuple.getT2())\n                        .timestamp(java.time.Instant.now())\n                        .build())\n                .subscribeOn(Schedulers.boundedElastic());\n    }\n    \n    public Mono<BigDecimal> getBestBid(String symbol) {\n        return orderRepository.findOrderBookLevels(symbol, OrderSide.BUY, 1)\n                .next()\n                .map(OrderBookLevel::getPrice)\n                .defaultIfEmpty(BigDecimal.ZERO);\n    }\n    \n    public Mono<BigDecimal> getBestAsk(String symbol) {\n        return orderRepository.findOrderBookLevels(symbol, OrderSide.SELL, 1)\n                .next()\n                .map(OrderBookLevel::getPrice)\n                .defaultIfEmpty(BigDecimal.ZERO);\n    }\n    \n    public Mono<BigDecimal> getSpread(String symbol) {\n        return Mono.zip(getBestBid(symbol), getBestAsk(symbol))\n                .map(tuple -> tuple.getT2().subtract(tuple.getT1()))\n                .filter(spread -> spread.compareTo(BigDecimal.ZERO) > 0)\n                .defaultIfEmpty(BigDecimal.ZERO);\n    }\n    \n    public void invalidateCache(String symbol) {\n        orderBookCache.remove(symbol);\n        log.debug(\"Invalidated order book cache for symbol: {}\", symbol);\n    }\n    \n    private Mono<OrderBookSnapshot> buildOrderBookSnapshot(String symbol, int levels) {\n        Mono<List<OrderBookLevel>> bidLevels = orderRepository\n                .findOrderBookLevels(symbol, OrderSide.BUY, levels)\n                .collectList();\n                \n        Mono<List<OrderBookLevel>> askLevels = orderRepository\n                .findOrderBookLevels(symbol, OrderSide.SELL, levels)\n                .collectList();\n                \n        return Mono.zip(bidLevels, askLevels)\n                .map(tuple -> {\n                    OrderBookSnapshot snapshot = OrderBookSnapshot.builder()\n                            .symbol(symbol)\n                            .bids(tuple.getT1())\n                            .asks(tuple.getT2())\n                            .timestamp(java.time.Instant.now())\n                            .build();\n                    \n                    orderBookCache.put(symbol, snapshot);\n                    return snapshot;\n                });\n    }\n}