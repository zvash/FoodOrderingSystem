package com.food.ordering.system.order.service.dataaccess.outbox.payment.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

    public PaymentOutboxRepositoryImpl(PaymentOutboxJpaRepository paymentOutboxJpaRepository,
                                       PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper) {
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
        this.paymentOutboxDataAccessMapper = paymentOutboxDataAccessMapper;
    }

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return paymentOutboxDataAccessMapper.paymentOutboxEntityToOrderPaymentOutboxMessage(
                paymentOutboxJpaRepository.save(
                        paymentOutboxDataAccessMapper.orderPaymentOutboxMessageToPaymentOutboxEntity(
                                orderPaymentOutboxMessage
                        )
                )
        );
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                            OutboxStatus outboxStatus,
                                                                                            SagaStatus... sagaStatus) {
        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                        outboxStatus,
                        Arrays.asList(sagaStatus))
                .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox object " +
                        "could not be found for saga type: " + type))
                .stream()
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage).toList()
        );

    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId,
                                                                                SagaStatus... sagaStatus) {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type,
                sagaId,
                Arrays.asList(sagaStatus)
        ).map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
                                                         SagaStatus... sagaStatus) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
                type,
                outboxStatus,
                Arrays.asList(sagaStatus)
        );
    }
}