package com.leonardo.orderprocessing.infrastructure.kafka;

import com.leonardo.orderprocessing.domain.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        BigDecimal amount,
        LocalDateTime createdAt
) {
    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(order.getId(), order.getAmount(), order.getCreatedAt());
    }
}
