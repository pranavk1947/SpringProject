package com.trading.common.exception;

public class ValidationException extends TradingException {
    
    private static final String ERROR_CODE = \"VALIDATION_ERROR\";\n    \n    public ValidationException(String message) {\n        super(message);\n    }\n    \n    @Override\n    public String getErrorCode() {\n        return ERROR_CODE;\n    }\n}