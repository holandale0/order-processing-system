package com.leonardo.orderprocessing.application.service;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.PaymentResult;
import com.leonardo.orderprocessing.domain.model.PaymentStrategy;
import com.leonardo.orderprocessing.domain.model.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    private static final BigDecimal FEE = new BigDecimal("0.03");

    @Override
    public PaymentType type() {
        return PaymentType.CREDIT_CARD;
    }

    @Override
    public PaymentResult process(Order order) {
        BigDecimal total = order.getAmount().multiply(BigDecimal.ONE.add(FEE)).setScale(2, RoundingMode.HALF_UP);
        log.info("[CREDIT_CARD] Processing payment for orderId={} amount={} total={}", order.getId(), order.getAmount(), total);
        return new PaymentResult(true, total, "Credit card charged with 3% fee");
    }
}
