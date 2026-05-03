package com.leonardo.orderprocessing.application.service;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.PaymentResult;
import com.leonardo.orderprocessing.domain.model.PaymentStrategy;
import com.leonardo.orderprocessing.domain.model.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebitCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentType type() {
        return PaymentType.DEBIT_CARD;
    }

    @Override
    public PaymentResult process(Order order) {
        log.info("[DEBIT_CARD] Processing payment for orderId={} amount={}", order.getId(), order.getAmount());
        return new PaymentResult(true, order.getAmount(), "Debit card charged successfully");
    }
}
