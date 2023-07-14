package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.exception.PaymentDomainException;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderOutboxHelper {

    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    public OrderOutboxHelper(OrderOutboxRepository orderOutboxRepository, ObjectMapper objectMapper) {
        this.orderOutboxRepository = orderOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage>
    getOrderOutboxMessageBySagaIdAndPaymentStatusAndSagaStatus(UUID sagaId,
                                                               PaymentStatus paymentStatus,
                                                               OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                sagaId,
                paymentStatus,
                outboxStatus
        );
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessagesByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderOutboxMessage with outbox id: {}",
                    orderOutboxMessage.getId());
            throw new PaymentDomainException("Could not save OrderOutboxMessage with outbox id: " +
                    orderOutboxMessage.getId());
        }
        log.info("OrderOutboxMessage saved with outbox id: {}", orderOutboxMessage.getId());
    }

    @Transactional
    public void saveOrderOutboxMessage(OrderEventPayload outboxEventPayload,
                                       PaymentStatus paymentStatus,
                                       OutboxStatus outboxStatus,
                                       UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(outboxEventPayload.getCreatedAt())
                .paymentStatus(paymentStatus)
                .outboxStatus(outboxStatus)
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(outboxEventPayload))
                .build());
    }

    @Transactional
    public void deleteOrderOutboxMessage(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload object for order id: {}",
                    orderEventPayload.getOrderId());
            throw new PaymentDomainException("Could not create OrderEventPayload object for order id: " +
                    orderEventPayload.getOrderId());
        }
    }

}
