package com.leonardo.orderprocessing.application.usecase;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderCreatedEvent;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    public Order execute(BigDecimal amount) {
        Order order = Order.newOrder(amount);
        Order saved = orderRepository.save(order);
        eventProducer.publishOrderCreated(OrderCreatedEvent.from(saved));
        return saved;
    }
}
