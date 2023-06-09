package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMessagingDataMapper {

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

    public RestaurantApprovalResponseAvroModel orderApprovedEventToRestaurantApprovalResponseAvroModel(
            OrderApprovedEvent orderApprovedEvent
    ) {
        return orderApprovalEventToRestaurantApprovalResponseAvroModel(orderApprovedEvent);
    }

    public RestaurantApprovalResponseAvroModel orderRejectedEventToRestaurantApprovalResponseAvroModel(
            OrderRejectedEvent orderRejectedEvent
    ) {
        return orderApprovalEventToRestaurantApprovalResponseAvroModel(orderRejectedEvent);
    }

    private RestaurantApprovalResponseAvroModel orderApprovalEventToRestaurantApprovalResponseAvroModel(
            OrderApprovalEvent orderApprovalEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setRestaurantId(orderApprovalEvent.getRestaurantId().getValue().toString())
                .setOrderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovalEvent.getOrderApproval()
                        .getApprovalStatus().name()))
                .setCreatedAt(orderApprovalEvent.getCreatedAt().toInstant())
                .setFailureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }
}
