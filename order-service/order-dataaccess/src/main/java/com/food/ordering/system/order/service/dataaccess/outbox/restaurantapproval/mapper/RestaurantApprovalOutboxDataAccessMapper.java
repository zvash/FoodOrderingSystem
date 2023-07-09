package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.RestaurantApprovalOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class RestaurantApprovalOutboxDataAccessMapper {

    public RestaurantApprovalOutboxEntity OrderApprovalOutboxMessageToRestaurantApprovalOutboxEntity(
            OrderApprovalOutboxMessage orderApprovalOutboxMessage
    ) {
        return RestaurantApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .processedAt(orderApprovalOutboxMessage.getProcessedAt())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage restaurantApprovalOutboxEntityToOrderApprovalOutboxMessage(
            RestaurantApprovalOutboxEntity restaurantApprovalOutboxEntity
    ) {
        return OrderApprovalOutboxMessage.builder()
                .id(restaurantApprovalOutboxEntity.getId())
                .sagaId(restaurantApprovalOutboxEntity.getSagaId())
                .createdAt(restaurantApprovalOutboxEntity.getCreatedAt())
                .processedAt(restaurantApprovalOutboxEntity.getProcessedAt())
                .outboxStatus(restaurantApprovalOutboxEntity.getOutboxStatus())
                .sagaStatus(restaurantApprovalOutboxEntity.getSagaStatus())
                .orderStatus(restaurantApprovalOutboxEntity.getOrderStatus())
                .version(restaurantApprovalOutboxEntity.getVersion())
                .build();
    }
}
