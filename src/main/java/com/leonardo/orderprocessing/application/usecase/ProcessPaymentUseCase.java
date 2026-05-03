package com.leonardo.orderprocessing.application.usecase;

import com.leonardo.orderprocessing.application.service.PaymentStrategyFactory;
import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.PaymentResult;
import com.leonardo.orderprocessing.domain.model.PaymentStrategy;
import com.leonardo.orderprocessing.domain.model.PaymentType;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderEventProducer;
import com.leonardo.orderprocessing.infrastructure.kafka.PaymentConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final OrderRepository orderRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final OrderEventProducer eventProducer;

    public PaymentResult execute(UUID orderId, PaymentType paymentType) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        log.info("[PAYMENT] Starting payment for orderId={} type={}", orderId, paymentType);

        PaymentStrategy strategy = paymentStrategyFactory.create(paymentType);
        PaymentResult result = strategy.process(order);

        if (result.success()) {
            order.confirm();
            orderRepository.save(order);
            log.info("[PAYMENT] Success for orderId={} charged={}", orderId, result.amountCharged());
            eventProducer.publishPaymentConfirmed(PaymentConfirmedEvent.from(orderId, paymentType, result));
        } else {
            order.cancel();
            orderRepository.save(order);
            log.warn("[PAYMENT] Failed for orderId={} details={}", orderId, result.details());
        }

        return result;
    }
}
