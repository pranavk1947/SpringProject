package com.trading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Hooks;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TradingSystemApplication {

    public static void main(String[] args) {
        // Enable reactor context propagation for better tracing
        Hooks.enableAutomaticContextPropagation();
        
        log.info("Starting Real-Time Financial Trading System...");
        SpringApplication.run(TradingSystemApplication.class, args);
        log.info("Real-Time Financial Trading System started successfully!");
    }
}