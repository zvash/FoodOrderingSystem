package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalResponseAvroModel
    orderEventPayloadToRestaurantApprovalResponseAvroModel(String sagaId, OrderEventPayload payload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setRestaurantId(payload.getRestaurantId())
                .setOrderId(payload.getOrderId())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(payload.getApprovalStatus()))
                .setCreatedAt(payload.getCreatedAt().toInstant())
                .setFailureMessages(payload.getFailureMessages())
                .build();
    }

    public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel
    ) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                        .getRestaurantOrderStatus().name()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .products(restaurantApprovalRequestAvroModel.getProducts().stream().map(product ->
                        Product.Builder.builder()
                                .productId(new ProductId(UUID.fromString(product.getId())))
                                .quantity(product.getQuantity())
                                .build()
                ).toList())
                .build();
    }
}
