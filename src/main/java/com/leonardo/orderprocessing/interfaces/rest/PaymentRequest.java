package com.leonardo.orderprocessing.interfaces.rest;

import com.leonardo.orderprocessing.domain.model.PaymentType;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull(message = "paymentType is required")
        PaymentType paymentType
) {}
