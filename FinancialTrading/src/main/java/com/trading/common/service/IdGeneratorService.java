package com.trading.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class IdGeneratorService {
    
    private final AtomicLong counter = new AtomicLong(0);
    private final String nodeId;\n    \n    public IdGeneratorService() {\n        // Simple node ID generation - in production, use proper distributed ID generation\n        this.nodeId = String.format(\"%03d\", System.currentTimeMillis() % 1000);\n    }\n    \n    public String generateOrderId() {\n        long timestamp = Instant.now().toEpochMilli();\n        long sequence = counter.getAndIncrement() % 10000;\n        return String.format(\"ORD-%s-%d-%04d\", nodeId, timestamp, sequence);\n    }\n    \n    public String generateTradeId() {\n        long timestamp = Instant.now().toEpochMilli();\n        long sequence = counter.getAndIncrement() % 10000;\n        return String.format(\"TRD-%s-%d-%04d\", nodeId, timestamp, sequence);\n    }\n    \n    public String generateTransactionId() {\n        long timestamp = Instant.now().toEpochMilli();\n        long sequence = counter.getAndIncrement() % 10000;\n        return String.format(\"TXN-%s-%d-%04d\", nodeId, timestamp, sequence);\n    }\n}