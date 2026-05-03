package com.leonardo.orderprocessing.application.usecase;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.OrderStatus;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ValidateOrderUseCase validateOrderUseCase;

    @Test
    void shouldUpdateOrderStatusToValidated() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, OrderStatus.PENDING, new BigDecimal("100.00"), LocalDateTime.now());
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        // Act
        validateOrderUseCase.execute(orderId);

        // Verify
        verify(orderRepository).save(captor.capture());

        // Assert
        assertThat(captor.getValue().getStatus()).isEqualTo(OrderStatus.VALIDATED);
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> validateOrderUseCase.execute(orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(orderId.toString());
    }
}
