package com.leonardo.orderprocessing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

    private UUID id;
    private OrderStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Order(UUID id, OrderStatus status, BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static Order newOrder(BigDecimal amount) {
        return new Order(UUID.randomUUID(), OrderStatus.PENDING, amount, LocalDateTime.now());
    }

    public UUID getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void validate() { this.status = OrderStatus.VALIDATED; }
    public void confirm() { this.status = OrderStatus.CONFIRMED; }
    public void cancel() { this.status = OrderStatus.CANCELLED; }
}
