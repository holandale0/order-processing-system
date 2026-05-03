package com.leonardo.orderprocessing.infrastructure.kafka;

import com.leonardo.orderprocessing.application.usecase.ValidateOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final ValidateOrderUseCase validateOrderUseCase;

    @Transactional
    @KafkaListener(topics = "order.created", groupId = "order-processing-group")
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("[RECEIVED] OrderCreatedEvent id={} amount={}", event.orderId(), event.amount());
        validateOrderUseCase.execute(event.orderId());
    }
}
