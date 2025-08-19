package com.trading.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@EnableR2dbcRepositories(basePackages = \"com.trading.*.repository\")
@EnableR2dbcAuditing
@RequiredArgsConstructor
public class DatabaseConfig {
    
    @Bean\n    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {\n        return new R2dbcTransactionManager(connectionFactory);\n    }\n    \n    @Bean\n    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {\n        return new R2dbcEntityTemplate(connectionFactory);\n    }\n}