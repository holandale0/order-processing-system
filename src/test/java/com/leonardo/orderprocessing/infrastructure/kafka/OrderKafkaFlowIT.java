package com.leonardo.orderprocessing.infrastructure.kafka;

import com.leonardo.orderprocessing.IntegrationTestBase;
import com.leonardo.orderprocessing.application.usecase.CreateOrderUseCase;
import com.leonardo.orderprocessing.domain.model.Order;
import com.leonardo.orderprocessing.domain.model.OrderStatus;
import com.leonardo.orderprocessing.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class OrderKafkaFlowIT extends IntegrationTestBase {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldTransitionOrderFromPendingToValidatedAfterKafkaConsumption() {
        // Assemble & Act
        Order created = createOrderUseCase.execute(new BigDecimal("300.00"));
        assertThat(created.getStatus()).isEqualTo(OrderStatus.PENDING);

        // Assert — aguarda consumer processar e atualizar status para VALIDATED
        await()
            .atMost(15, TimeUnit.SECONDS)
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                Order updated = orderRepository.findById(created.getId()).orElseThrow();
                assertThat(updated.getStatus()).isEqualTo(OrderStatus.VALIDATED);
            });
    }
}
