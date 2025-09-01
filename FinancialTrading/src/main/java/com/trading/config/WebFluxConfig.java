package com.trading.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
public class WebFluxConfig implements WebFluxConfigurer {

    private static final String API_PATH_PATTERN = "/api/**";
    private static final int MAX_IN_MEMORY_SIZE = 1024 * 1024; // 1 MB
    private static final int CORS_MAX_AGE = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(API_PATH_PATTERN)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(CORS_MAX_AGE);
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
        configurer.defaultCodecs().maxInMemorySize(MAX_IN_MEMORY_SIZE);
    }

    @Bean(name = "tradingScheduler")
    public Scheduler tradingScheduler() {
        int parallelism = Runtime.getRuntime().availableProcessors() * 2;
        return Schedulers.newParallel("trading-scheduler", parallelism);
    }

    @Bean
    public RouterFunction<ServerResponse> healthRoutes() {
        return route(GET("/health"),
                request -> ServerResponse.ok().bodyValue("Trading system is healthy"));
    }
}
