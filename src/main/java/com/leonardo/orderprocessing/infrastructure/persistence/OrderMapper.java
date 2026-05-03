package com.leonardo.orderprocessing.infrastructure.persistence;

import com.leonardo.orderprocessing.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toDomain(OrderEntity entity) {
        return new Order(
                entity.getId(),
                entity.getStatus(),
                entity.getAmount(),
                entity.getCreatedAt()
        );
    }

    public OrderEntity toNewEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .status(order.getStatus())
                .amount(order.getAmount())
                .createdAt(order.getCreatedAt())
                .isNew(true)
                .build();
    }

    public OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .status(order.getStatus())
                .amount(order.getAmount())
                .createdAt(order.getCreatedAt())
                .isNew(false)
                .build();
    }
}
