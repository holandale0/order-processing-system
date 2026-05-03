package com.leonardo.orderprocessing.application.usecase;

import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.OrderStatus;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderCreatedEvent;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer eventProducer;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderWithPendingStatus() {
        // Assemble
        BigDecimal amount = new BigDecimal("150.00");
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Order result = createOrderUseCase.execute(amount);

        // Assert
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getAmount()).isEqualByComparingTo(amount);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldSaveOrderToRepository() {
        // Assemble
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        createOrderUseCase.execute(new BigDecimal("100.00"));

        // Verify
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldPublishOrderCreatedEvent() {
        // Assemble
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

        // Act
        Order result = createOrderUseCase.execute(new BigDecimal("200.00"));

        // Verify
        verify(eventProducer).publishOrderCreated(captor.capture());

        // Assert
        assertThat(captor.getValue().orderId()).isEqualTo(result.getId());
        assertThat(captor.getValue().amount()).isEqualByComparingTo(new BigDecimal("200.00"));
    }
}
