package com.trading.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.trading.order.Order;
import com.trading.order.dto.OrderEvent;
import com.trading.order.mapper.OrderMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisher {
    
    private static final String ORDER_EVENTS_TOPIC = \"order-events\";\n    private static final String ORDER_UPDATES_TOPIC = \"order-updates\";\n    \n    private final KafkaTemplate<String, Object> kafkaTemplate;\n    private final OrderMapper orderMapper;\n    \n    public Mono<Void> publishOrderEvent(Order order) {\n        OrderEvent event = OrderEvent.builder()\n                .orderId(order.getOrderId())\n                .userId(order.getUserId())\n                .symbol(order.getSymbol())\n                .eventType(determineEventType(order))\n                .order(orderMapper.toResponse(order))\n                .timestamp(java.time.Instant.now())\n                .build();\n                \n        return Mono.fromFuture(\n                kafkaTemplate.send(ORDER_EVENTS_TOPIC, order.getOrderId(), event)\n                        .completable()\n        )\n        .doOnSuccess(result -> log.debug(\"Published order event: {} for order: {}\", \n                event.getEventType(), order.getOrderId()))\n        .doOnError(error -> log.error(\"Failed to publish order event for order: {}\", \n                order.getOrderId(), error))\n        .then();\n    }\n    \n    public Mono<Void> publishOrderUpdate(Order order) {\n        return Mono.fromFuture(\n                kafkaTemplate.send(ORDER_UPDATES_TOPIC, order.getOrderId(), \n                        orderMapper.toResponse(order))\n                        .completable()\n        )\n        .doOnSuccess(result -> log.debug(\"Published order update for order: {}\", order.getOrderId()))\n        .doOnError(error -> log.error(\"Failed to publish order update for order: {}\", \n                order.getOrderId(), error))\n        .then();\n    }\n    \n    private String determineEventType(Order order) {\n        return switch (order.getStatus()) {\n            case PENDING -> \"ORDER_CREATED\";\n            case OPEN -> \"ORDER_OPENED\";\n            case PARTIALLY_FILLED -> \"ORDER_PARTIALLY_FILLED\";\n            case FILLED -> \"ORDER_FILLED\";\n            case CANCELLED -> \"ORDER_CANCELLED\";\n            case REJECTED -> \"ORDER_REJECTED\";\n            case EXPIRED -> \"ORDER_EXPIRED\";\n        };\n    }\n}