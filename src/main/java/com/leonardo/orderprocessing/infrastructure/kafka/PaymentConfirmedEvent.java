package com.leonardo.orderprocessing.infrastructure.kafka;

import com.leonardo.orderprocessing.domain.model.PaymentResult;
import com.leonardo.orderprocessing.domain.model.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentConfirmedEvent(
        UUID orderId,
        PaymentType paymentType,
        BigDecimal amountCharged,
        LocalDateTime confirmedAt
) {
    public static PaymentConfirmedEvent from(UUID orderId, PaymentType paymentType, PaymentResult result) {
        return new PaymentConfirmedEvent(orderId, paymentType, result.amountCharged(), LocalDateTime.now());
    }
}
