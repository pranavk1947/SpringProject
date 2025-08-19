package com.trading.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.trading.order.OrderType;
import com.trading.order.dto.CreateOrderRequest;
import com.trading.common.exception.ValidationException;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    
    private static final BigDecimal MAX_ORDER_SIZE = new BigDecimal("1000000\");\n    private static final BigDecimal MIN_PRICE = new BigDecimal(\"0.00000001\");\n    private static final BigDecimal MAX_PRICE = new BigDecimal(\"1000000\");\n    \n    public Mono<Void> validateOrderRequest(String userId, CreateOrderRequest request) {\n        return Mono.fromRunnable(() -> {\n            validateOrderSize(request.getQuantity());\n            validatePrice(request);\n            validateSymbol(request.getSymbol());\n            validateUserPermissions(userId, request.getSymbol());\n        });\n    }\n    \n    private void validateOrderSize(BigDecimal quantity) {\n        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {\n            throw new ValidationException(\"Order quantity must be positive\");\n        }\n        if (quantity.compareTo(MAX_ORDER_SIZE) > 0) {\n            throw new ValidationException(\"Order quantity exceeds maximum allowed: \" + MAX_ORDER_SIZE);\n        }\n    }\n    \n    private void validatePrice(CreateOrderRequest request) {\n        if (request.getType() == OrderType.LIMIT || request.getType() == OrderType.STOP_LIMIT) {\n            if (request.getPrice() == null) {\n                throw new ValidationException(\"Price is required for LIMIT and STOP_LIMIT orders\");\n            }\n            validatePriceRange(request.getPrice());\n        }\n        \n        if (request.getType() == OrderType.STOP_LOSS || request.getType() == OrderType.STOP_LIMIT) {\n            if (request.getStopPrice() == null) {\n                throw new ValidationException(\"Stop price is required for STOP orders\");\n            }\n            validatePriceRange(request.getStopPrice());\n        }\n    }\n    \n    private void validatePriceRange(BigDecimal price) {\n        if (price.compareTo(MIN_PRICE) < 0) {\n            throw new ValidationException(\"Price must be at least \" + MIN_PRICE);\n        }\n        if (price.compareTo(MAX_PRICE) > 0) {\n            throw new ValidationException(\"Price exceeds maximum allowed: \" + MAX_PRICE);\n        }\n    }\n    \n    private void validateSymbol(String symbol) {\n        if (symbol == null || symbol.trim().isEmpty()) {\n            throw new ValidationException(\"Symbol is required\");\n        }\n        if (symbol.length() > 10) {\n            throw new ValidationException(\"Symbol length cannot exceed 10 characters\");\n        }\n        // Add more symbol validation logic as needed\n    }\n    \n    private void validateUserPermissions(String userId, String symbol) {\n        // Add user permission validation logic\n        // For example, check if user is allowed to trade this symbol\n        log.debug(\"Validating user {} permissions for symbol {}\", userId, symbol);\n    }\n}