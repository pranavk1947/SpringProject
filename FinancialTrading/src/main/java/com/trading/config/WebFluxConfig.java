package com.trading.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
public class WebFluxConfig implements WebFluxConfigurer {
    
    @Override\n    public void addCorsMappings(CorsRegistry registry) {\n        registry.addMapping(\"/api/**\")\n                .allowedOriginPatterns(\"*\")\n                .allowedMethods(\"GET\", \"POST\", \"PUT\", \"DELETE\", \"OPTIONS\")\n                .allowedHeaders(\"*\")\n                .allowCredentials(true)\n                .maxAge(3600);\n    }\n    \n    @Override\n    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {\n        ObjectMapper objectMapper = new ObjectMapper()\n                .registerModule(new JavaTimeModule())\n                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);\n                \n        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));\n        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));\n        configurer.defaultCodecs().maxInMemorySize(1024 * 1024); // 1MB\n    }\n    \n    @Bean(name = \"tradingScheduler\")\n    public Scheduler tradingScheduler() {\n        return Schedulers.newParallel(\"trading-scheduler\", \n                Runtime.getRuntime().availableProcessors() * 2);\n    }\n    \n    @Bean\n    public RouterFunction<ServerResponse> healthRoutes() {\n        return route(GET(\"/health\"), \n                request -> ServerResponse.ok().bodyValue(\"Trading system is healthy\"));\n    }\n}