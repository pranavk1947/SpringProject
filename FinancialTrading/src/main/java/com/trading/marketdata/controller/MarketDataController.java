package com.trading.marketdata.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.trading.matching.OrderBookService;
import com.trading.matching.dto.MarketDepth;
import com.trading.trade.repository.TradeRepository;
import com.trading.trade.repository.TradingStatistics;
import com.trading.common.dto.ApiResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(\"/api/v1/market-data\")
@RequiredArgsConstructor
public class MarketDataController {
    
    private final OrderBookService orderBookService;
    private final TradeRepository tradeRepository;
    
    @GetMapping(\"/depth/{symbol}\")
    public Mono<ResponseEntity<ApiResponse<MarketDepth>>> getMarketDepth(\n            @PathVariable @NotBlank String symbol,\n            @RequestParam(defaultValue = \"10\") @Min(1) int levels) {\n        \n        return orderBookService.getMarketDepth(symbol, levels)\n                .map(depth -> ResponseEntity.ok(\n                        ApiResponse.success(\"Market depth retrieved successfully\", depth)));\n    }\n    \n    @GetMapping(\"/spread/{symbol}\")\n    public Mono<ResponseEntity<ApiResponse<BigDecimal>>> getSpread(\n            @PathVariable @NotBlank String symbol) {\n        \n        return orderBookService.getSpread(symbol)\n                .map(spread -> ResponseEntity.ok(\n                        ApiResponse.success(\"Spread retrieved successfully\", spread)));\n    }\n    \n    @GetMapping(\"/best-prices/{symbol}\")\n    public Mono<ResponseEntity<ApiResponse<BestPrices>>> getBestPrices(\n            @PathVariable @NotBlank String symbol) {\n        \n        return Mono.zip(\n                orderBookService.getBestBid(symbol),\n                orderBookService.getBestAsk(symbol)\n        )\n        .map(tuple -> BestPrices.builder()\n                .symbol(symbol)\n                .bestBid(tuple.getT1())\n                .bestAsk(tuple.getT2())\n                .timestamp(java.time.Instant.now())\n                .build())\n        .map(prices -> ResponseEntity.ok(\n                ApiResponse.success(\"Best prices retrieved successfully\", prices)));\n    }\n    \n    @GetMapping(\"/trades/{symbol}/recent\")\n    public Flux<com.trading.trade.Trade> getRecentTrades(\n            @PathVariable @NotBlank String symbol,\n            @RequestParam(defaultValue = \"100\") @Min(1) int limit) {\n        \n        LocalDateTime since = LocalDateTime.now().minusHours(1);\n        return tradeRepository.findRecentTrades(symbol, since, limit);\n    }\n    \n    @GetMapping(\"/statistics/{symbol}\")\n    public Mono<ResponseEntity<ApiResponse<TradingStatistics>>> getTradingStatistics(\n            @PathVariable @NotBlank String symbol,\n            @RequestParam(defaultValue = \"24\") @Min(1) int hours) {\n        \n        LocalDateTime since = LocalDateTime.now().minusHours(hours);\n        return tradeRepository.getTradingStatistics(symbol, since)\n                .map(stats -> ResponseEntity.ok(\n                        ApiResponse.success(\"Trading statistics retrieved successfully\", stats)));\n    }\n    \n    @GetMapping(\"/volume/{symbol}\")\n    public Mono<ResponseEntity<ApiResponse<BigDecimal>>> getTradingVolume(\n            @PathVariable @NotBlank String symbol,\n            @RequestParam(defaultValue = \"24\") @Min(1) int hours) {\n        \n        LocalDateTime since = LocalDateTime.now().minusHours(hours);\n        return tradeRepository.getTradingVolume(symbol, since)\n                .map(volume -> ResponseEntity.ok(\n                        ApiResponse.success(\"Trading volume retrieved successfully\", volume)));\n    }\n    \n    @lombok.Data\n    @lombok.Builder\n    @lombok.NoArgsConstructor\n    @lombok.AllArgsConstructor\n    public static class BestPrices {\n        private String symbol;\n        private BigDecimal bestBid;\n        private BigDecimal bestAsk;\n        \n        @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING)\n        private java.time.Instant timestamp;\n        \n        public BigDecimal getSpread() {\n            if (bestBid.compareTo(BigDecimal.ZERO) > 0 && bestAsk.compareTo(BigDecimal.ZERO) > 0) {\n                return bestAsk.subtract(bestBid);\n            }\n            return BigDecimal.ZERO;\n        }\n    }\n}