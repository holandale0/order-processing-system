package com.leonardo.orderprocessing.domain.model;

public interface PaymentStrategy {

    PaymentType type();

    PaymentResult process(Order order);
}
