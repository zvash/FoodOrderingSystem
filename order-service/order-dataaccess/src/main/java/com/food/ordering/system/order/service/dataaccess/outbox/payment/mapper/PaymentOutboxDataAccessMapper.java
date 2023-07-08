package com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

public class PaymentOutboxDataAccessMapper {

    public PaymentOutboxEntity orderPaymentOutboxMessageToPaymentOutboxEntity(
            OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .processedAt(orderPaymentOutboxMessage.getProcessedAt())
                .type(orderPaymentOutboxMessage.getType())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage paymentOutboxEntityToOrderPaymentOutboxMessage(PaymentOutboxEntity
                                                                                            paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .processedAt(paymentOutboxEntity.getProcessedAt())
                .type(paymentOutboxEntity.getType())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .sagaStatus(paymentOutboxEntity.getSagaStatus())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }
}
