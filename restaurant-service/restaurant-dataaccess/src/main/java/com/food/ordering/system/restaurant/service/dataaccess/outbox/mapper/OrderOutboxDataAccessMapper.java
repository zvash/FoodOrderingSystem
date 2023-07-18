package com.food.ordering.system.restaurant.service.dataaccess.outbox.mapper;

import com.food.ordering.system.restaurant.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity orderOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(orderOutboxEntity.getId())
                .sagaId(orderOutboxEntity.getSagaId())
                .createdAt(orderOutboxEntity.getCreatedAt())
                .processedAt(orderOutboxEntity.getProcessedAt())
                .type(orderOutboxEntity.getType())
                .payload(orderOutboxEntity.getPayload())
                .outboxStatus(orderOutboxEntity.getOutboxStatus())
                .approvalStatus(orderOutboxEntity.getApprovalStatus())
                .version(orderOutboxEntity.getVersion())
                .build();
    }

    public OrderOutboxEntity orderOutboxMessageToOrderOutboxEntity(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .processedAt(orderOutboxMessage.getProcessedAt())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .approvalStatus(orderOutboxMessage.getApprovalStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
    }
}
