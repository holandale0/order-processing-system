package com.leonardo.orderprocessing.interfaces.rest;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        OrderStatus status,
        BigDecimal amount,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getAmount(),
                order.getCreatedAt()
        );
    }
}
