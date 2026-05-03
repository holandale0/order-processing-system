package com.leonardo.orderprocessing.interfaces.rest;

import com.leonardo.orderprocessing.domain.model.PaymentResult;

import java.math.BigDecimal;

public record PaymentResponse(
        boolean success,
        BigDecimal amountCharged,
        String details
) {
    public static PaymentResponse from(PaymentResult result) {
        return new PaymentResponse(result.success(), result.amountCharged(), result.details());
    }
}
