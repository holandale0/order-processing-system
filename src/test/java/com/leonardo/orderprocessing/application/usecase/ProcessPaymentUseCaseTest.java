package com.leonardo.orderprocessing.application.usecase;

import com.leonardo.orderprocessing.application.service.PaymentStrategyFactory;
import com.leonardo.orderprocessing.domain.model.*;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderEventProducer;
import com.leonardo.orderprocessing.infrastructure.kafka.PaymentConfirmedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private OrderEventProducer eventProducer;

    @InjectMocks
    private ProcessPaymentUseCase processPaymentUseCase;

    private Order pendingOrder(UUID id) {
        return new Order(id, OrderStatus.PENDING, new BigDecimal("100.00"), LocalDateTime.now());
    }

    @Test
    void shouldConfirmOrderOnSuccessfulPayment() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId);
        PaymentResult successResult = new PaymentResult(true, new BigDecimal("100.00"), "ok");
        PaymentStrategy strategy = mock(PaymentStrategy.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentStrategyFactory.create(PaymentType.PIX)).thenReturn(strategy);
        when(strategy.process(order)).thenReturn(successResult);
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        // Act
        processPaymentUseCase.execute(orderId, PaymentType.PIX);

        // Verify
        verify(orderRepository).save(captor.capture());

        // Assert
        assertThat(captor.getValue().getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void shouldCancelOrderOnFailedPayment() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId);
        PaymentResult failResult = new PaymentResult(false, BigDecimal.ZERO, "declined");
        PaymentStrategy strategy = mock(PaymentStrategy.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentStrategyFactory.create(PaymentType.CREDIT_CARD)).thenReturn(strategy);
        when(strategy.process(order)).thenReturn(failResult);
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        // Act
        processPaymentUseCase.execute(orderId, PaymentType.CREDIT_CARD);

        // Verify
        verify(orderRepository).save(captor.capture());

        // Assert
        assertThat(captor.getValue().getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void shouldPublishPaymentConfirmedEventOnSuccess() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId);
        PaymentResult successResult = new PaymentResult(true, new BigDecimal("100.00"), "ok");
        PaymentStrategy strategy = mock(PaymentStrategy.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentStrategyFactory.create(PaymentType.PIX)).thenReturn(strategy);
        when(strategy.process(order)).thenReturn(successResult);
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<PaymentConfirmedEvent> captor = ArgumentCaptor.forClass(PaymentConfirmedEvent.class);

        // Act
        processPaymentUseCase.execute(orderId, PaymentType.PIX);

        // Verify
        verify(eventProducer).publishPaymentConfirmed(captor.capture());

        // Assert
        assertThat(captor.getValue().orderId()).isEqualTo(orderId);
        assertThat(captor.getValue().paymentType()).isEqualTo(PaymentType.PIX);
    }

    @Test
    void shouldNotPublishEventOnFailedPayment() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId);
        PaymentResult failResult = new PaymentResult(false, BigDecimal.ZERO, "declined");
        PaymentStrategy strategy = mock(PaymentStrategy.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentStrategyFactory.create(PaymentType.DEBIT_CARD)).thenReturn(strategy);
        when(strategy.process(order)).thenReturn(failResult);
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        processPaymentUseCase.execute(orderId, PaymentType.DEBIT_CARD);

        // Verify
        verify(eventProducer, never()).publishPaymentConfirmed(any());
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> processPaymentUseCase.execute(orderId, PaymentType.PIX))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(orderId.toString());
    }
}
