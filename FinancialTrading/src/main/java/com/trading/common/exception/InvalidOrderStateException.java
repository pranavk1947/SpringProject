package com.trading.common.exception;

public class InvalidOrderStateException extends TradingException {
    
    private static final String ERROR_CODE = \"INVALID_ORDER_STATE\";\n    \n    public InvalidOrderStateException(String message) {\n        super(message);\n    }\n    \n    @Override\n    public String getErrorCode() {\n        return ERROR_CODE;\n    }\n}