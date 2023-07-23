package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOutboxDataAccessMapper {

    public ApprovalOutboxEntity OrderApprovalOutboxMessageToApprovalOutboxEntity(
            OrderApprovalOutboxMessage orderApprovalOutboxMessage
    ) {
        return ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .processedAt(orderApprovalOutboxMessage.getProcessedAt())
                .type(orderApprovalOutboxMessage.getType())
                .payload(orderApprovalOutboxMessage.getPayload())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage approvalOutboxEntityToOrderApprovalOutboxMessage(
            ApprovalOutboxEntity restaurantApprovalOutboxEntity
    ) {
        return OrderApprovalOutboxMessage.builder()
                .id(restaurantApprovalOutboxEntity.getId())
                .sagaId(restaurantApprovalOutboxEntity.getSagaId())
                .createdAt(restaurantApprovalOutboxEntity.getCreatedAt())
                .processedAt(restaurantApprovalOutboxEntity.getProcessedAt())
                .type(restaurantApprovalOutboxEntity.getType())
                .payload(restaurantApprovalOutboxEntity.getPayload())
                .outboxStatus(restaurantApprovalOutboxEntity.getOutboxStatus())
                .sagaStatus(restaurantApprovalOutboxEntity.getSagaStatus())
                .orderStatus(restaurantApprovalOutboxEntity.getOrderStatus())
                .version(restaurantApprovalOutboxEntity.getVersion())
                .build();
    }
}
