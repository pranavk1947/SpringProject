package com.trading.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

/**
 * Database configuration for the Financial Trading System.
 * Configures R2DBC for reactive database operations with proper
 * connection pooling, transaction management, and schema initialization.
 */
@Configuration
@EnableR2dbcRepositories(basePackages = {
    "com.trading.order.repository",
    "com.trading.trade.repository",
    "com.trading.user.repository",
    "com.trading.portfolio.repository",
    "com.trading.risk.repository"
})
@EnableR2dbcAuditing
@EnableTransactionManagement
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfig {
    
    private final R2dbcProperties r2dbcProperties;
    
    /**
     * Configures reactive transaction manager for database operations.
     * Essential for maintaining ACID properties in financial transactions.
     */
    @Bean
    @Primary
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        log.info("Configuring reactive transaction manager for financial trading system");
        R2dbcTransactionManager transactionManager = new R2dbcTransactionManager(connectionFactory);
        transactionManager.setEnforceReadOnly(true);
        return transactionManager;
    }
    
    /**
     * R2DBC Entity Template for custom database operations.
     * Provides fine-grained control over database queries and transactions.
     */
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        log.info("Configuring R2DBC entity template");
        return new R2dbcEntityTemplate(connectionFactory);
    }
    
    /**
     * Database client for low-level database operations.
     * Used for complex queries and batch operations.
     */
    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .namedParameters(true)
                .build();
    }
    
    /**
     * Database schema initializer.
     * Ensures database schema is properly set up on application startup.
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        
        // Initialize schema if schema.sql exists
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        try {
            populator.addScript(new ClassPathResource("schema.sql"));
            initializer.setDatabasePopulator(populator);
            log.info("Database schema initialization configured");
        } catch (Exception e) {
            log.warn("No schema.sql found, skipping database initialization");
        }
        
        return initializer;
    }
    
    /**
     * Database health check on application startup.
     * Verifies database connectivity and logs connection status.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void verifyDatabaseConnection(ApplicationReadyEvent event) {
        DatabaseClient client = event.getApplicationContext().getBean(DatabaseClient.class);
        
        client.sql("SELECT 1 as health_check")
                .fetch()
                .first()
                .doOnSuccess(result -> log.info("Database connection verified successfully: {}", result))
                .doOnError(error -> log.error("Database connection failed: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.error("Critical: Unable to connect to database. Trading system cannot start.", error);
                    return Mono.empty();
                })
                .subscribe();
    }
}