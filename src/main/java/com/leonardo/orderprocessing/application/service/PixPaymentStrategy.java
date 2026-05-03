package com.leonardo.orderprocessing.application.service;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.PaymentResult;
import com.leonardo.orderprocessing.domain.model.PaymentStrategy;
import com.leonardo.orderprocessing.domain.model.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PixPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentType type() {
        return PaymentType.PIX;
    }

    @Override
    public PaymentResult process(Order order) {
        log.info("[PIX] Processing payment for orderId={} amount={}", order.getId(), order.getAmount());
        return new PaymentResult(true, order.getAmount(), "PIX key generated successfully");
    }
}
