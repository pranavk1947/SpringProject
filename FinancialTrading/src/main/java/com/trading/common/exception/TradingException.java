package com.trading.common.exception;

public abstract class TradingException extends RuntimeException {
    
    public TradingException(String message) {
        super(message);
    }
    
    public TradingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public abstract String getErrorCode();
}