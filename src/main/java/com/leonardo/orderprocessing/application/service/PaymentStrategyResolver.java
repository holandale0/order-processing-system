package com.leonardo.orderprocessing.application.service;

import com.leonardo.orderprocessing.domain.model.PaymentStrategy;
import com.leonardo.orderprocessing.domain.model.PaymentType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentStrategyResolver {

    private final Map<PaymentType, PaymentStrategy> strategies;

    public PaymentStrategyResolver(List<PaymentStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(PaymentStrategy::type, Function.identity()));
    }

    public PaymentStrategy resolve(PaymentType type) {
        PaymentStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No payment strategy found for type: " + type);
        }
        return strategy;
    }
}
