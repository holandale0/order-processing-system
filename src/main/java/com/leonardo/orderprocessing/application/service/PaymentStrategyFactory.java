package com.leonardo.orderprocessing.application.service;

import com.leonardo.orderprocessing.domain.model.PaymentStrategy;
import com.leonardo.orderprocessing.domain.model.PaymentType;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    private final PixPaymentStrategy pix;
    private final CreditCardPaymentStrategy creditCard;
    private final DebitCardPaymentStrategy debitCard;

    public PaymentStrategyFactory(PixPaymentStrategy pix,
                                  CreditCardPaymentStrategy creditCard,
                                  DebitCardPaymentStrategy debitCard) {
        this.pix = pix;
        this.creditCard = creditCard;
        this.debitCard = debitCard;
    }

    public PaymentStrategy create(PaymentType type) {
        return switch (type) {
            case PIX         -> pix;
            case CREDIT_CARD -> creditCard;
            case DEBIT_CARD  -> debitCard;
        };
    }
}
