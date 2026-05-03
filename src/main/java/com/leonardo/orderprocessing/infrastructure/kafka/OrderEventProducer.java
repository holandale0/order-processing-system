package com.leonardo.orderprocessing.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String TOPIC_ORDER_CREATED    = "order.created";
    private static final String TOPIC_PAYMENT_CONFIRMED = "payment.confirmed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event) {
        publish(TOPIC_ORDER_CREATED, event.orderId().toString(), event);
    }

    public void publishPaymentConfirmed(PaymentConfirmedEvent event) {
        publish(TOPIC_PAYMENT_CONFIRMED, event.orderId().toString(), event);
    }

    private void publish(String topic, String key, Object payload) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[KAFKA] Failed to publish to topic={} key={}", topic, key, ex);
                    } else {
                        log.info("[KAFKA] Published to topic={} key={}", topic, key);
                    }
                });
    }
}
