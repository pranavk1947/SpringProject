package com.trading.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import com.trading.common.dto.ApiResponse;
import com.trading.common.exception.TradingException;
import com.trading.common.exception.OrderNotFoundException;
import com.trading.common.exception.InvalidOrderStateException;
import com.trading.common.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(OrderNotFoundException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleOrderNotFound(OrderNotFoundException ex) {
        log.warn(\"Order not found: {}\", ex.getMessage());\n        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());\n        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(response));\n    }\n    \n    @ExceptionHandler(InvalidOrderStateException.class)\n    public Mono<ResponseEntity<ApiResponse<Void>>> handleInvalidOrderState(InvalidOrderStateException ex) {\n        log.warn(\"Invalid order state: {}\", ex.getMessage());\n        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());\n        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));\n    }\n    \n    @ExceptionHandler(ValidationException.class)\n    public Mono<ResponseEntity<ApiResponse<Void>>> handleValidation(ValidationException ex) {\n        log.warn(\"Validation error: {}\", ex.getMessage());\n        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());\n        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));\n    }\n    \n    @ExceptionHandler(WebExchangeBindException.class)\n    public Mono<ResponseEntity<ApiResponse<Map<String, String>>>> handleValidationErrors(\n            WebExchangeBindException ex) {\n        Map<String, String> errors = new HashMap<>();\n        ex.getBindingResult().getAllErrors().forEach(error -> {\n            String fieldName = ((FieldError) error).getField();\n            String errorMessage = error.getDefaultMessage();\n            errors.put(fieldName, errorMessage);\n        });\n        \n        log.warn(\"Validation errors: {}\", errors);\n        ApiResponse<Map<String, String>> response = ApiResponse.error(\n                \"Validation failed\", \"VALIDATION_ERROR\");\n        response.setData(errors);\n        \n        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));\n    }\n    \n    @ExceptionHandler(TradingException.class)\n    public Mono<ResponseEntity<ApiResponse<Void>>> handleTradingException(TradingException ex) {\n        log.error(\"Trading system error: {}\", ex.getMessage(), ex);\n        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());\n        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));\n    }\n    \n    @ExceptionHandler(Exception.class)\n    public Mono<ResponseEntity<ApiResponse<Void>>> handleGenericException(Exception ex) {\n        log.error(\"Unexpected error occurred\", ex);\n        ApiResponse<Void> response = ApiResponse.error(\n                \"An unexpected error occurred\", \"INTERNAL_SERVER_ERROR\");\n        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));\n    }\n}