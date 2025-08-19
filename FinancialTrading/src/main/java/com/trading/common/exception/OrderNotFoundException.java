package com.trading.common.exception;

public class OrderNotFoundException extends TradingException {
    
    private static final String ERROR_CODE = \"ORDER_NOT_FOUND\";\n    \n    public OrderNotFoundException(String orderId) {\n        super(\"Order not found: \" + orderId);\n    }\n    \n    @Override\n    public String getErrorCode() {\n        return ERROR_CODE;\n    }\n}