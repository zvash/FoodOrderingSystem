package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper) {
        this.paymentRequestHelper = paymentRequestHelper;
    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
        log.info("Publishing payment completed event with payment id: {} and order id: {}",
                paymentRequest.getId(),
                paymentRequest.getOrderId());
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistCancelPayment(paymentRequest);
        log.info("Publishing payment cancelled event with payment id: {} and order id: {}",
                paymentRequest.getId(),
                paymentRequest.getOrderId());
    }
}
