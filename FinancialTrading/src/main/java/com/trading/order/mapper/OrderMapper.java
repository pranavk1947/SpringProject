package com.trading.order.mapper;

import com.trading.order.Order;
import com.trading.order.dto.CreateOrderRequest;
import com.trading.order.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "filledQuantity", constant = "0")
    @Mapping(target = "averagePrice", constant = "0")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(CreateOrderRequest request);
    
    OrderResponse toResponse(Order order);
}