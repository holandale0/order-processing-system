package com.leonardo.orderprocessing.domain.model;

import java.math.BigDecimal;

public record PaymentResult(
        boolean success,
        BigDecimal amountCharged,
        String details
) {}
