package com.leonardo.orderprocessing.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventConsumer {

    @KafkaListener(topics = "payment.confirmed", groupId = "order-processing-group")
    public void onPaymentConfirmed(PaymentConfirmedEvent event) {
        log.info("[PAYMENT CONFIRMED] orderId={} type={} charged={} at={}",
                event.orderId(), event.paymentType(), event.amountCharged(), event.confirmedAt());
    }
}
