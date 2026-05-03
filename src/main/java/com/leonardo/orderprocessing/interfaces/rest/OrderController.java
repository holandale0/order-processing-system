package com.leonardo.orderprocessing.interfaces.rest;

import com.leonardo.orderprocessing.application.usecase.CreateOrderUseCase;
import com.leonardo.orderprocessing.application.usecase.ProcessPaymentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody @Valid OrderRequest request) {
        return OrderResponse.from(createOrderUseCase.execute(request.amount()));
    }

    @PostMapping("/{id}/payment")
    public PaymentResponse processPayment(@PathVariable UUID id,
                                          @RequestBody @Valid PaymentRequest request) {
        return PaymentResponse.from(processPaymentUseCase.execute(id, request.paymentType()));
    }
}
