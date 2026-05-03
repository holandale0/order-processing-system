package com.leonardo.orderprocessing.application.usecase;

import com.leonardo.orderprocessing.domain.model.OrderStatus;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderUseCase {

    private final OrderRepository orderRepository;

    public void execute(UUID orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("[VALIDATE] Order id={} already in status={}, skipping", orderId, order.getStatus());
            return;
        }

        log.info("[PROCESSING] Order id={} status={} amount={}", order.getId(), order.getStatus(), order.getAmount());

        order.validate();
        orderRepository.save(order);

        log.info("[VALIDATED] Order id={} status={}", order.getId(), order.getStatus());
    }
}
