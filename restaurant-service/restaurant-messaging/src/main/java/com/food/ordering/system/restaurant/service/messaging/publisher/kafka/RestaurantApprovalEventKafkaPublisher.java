package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class RestaurantApprovalEventKafkaPublisher implements RestaurantApprovalResponseMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public RestaurantApprovalEventKafkaPublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                                 KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                                 RestaurantServiceConfigData restaurantServiceConfigData,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage,
            OutboxStatus> outboxCallback) {
        OrderEventPayload payload = kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(),
                OrderEventPayload.class);
        String sagaId = orderOutboxMessage.getSagaId().toString();
        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}",
                payload.getOrderId(), sagaId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = restaurantMessagingDataMapper
                    .orderEventPayloadToRestaurantApprovalResponseAvroModel(sagaId, payload);

            kafkaProducer.send(
                    restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    sagaId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderOutboxMessage,
                            outboxCallback,
                            payload.getOrderId(),
                            "RestaurantApprovalResponseAvroModel"
                    )
            );
            log.info("OrderEventPayload sent to kafka for  order id: {} and saga id: {}",
                    payload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderEventPayload " +
                            "to kafka for  order id: {} and saga id: {}, error: {}",
                    payload.getOrderId(), sagaId, e.getMessage());
        }
    }
}
