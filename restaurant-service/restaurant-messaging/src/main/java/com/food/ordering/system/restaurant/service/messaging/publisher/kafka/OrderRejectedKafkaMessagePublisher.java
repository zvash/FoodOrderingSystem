package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel>
            restaurantApprovalResponseAvroModelKafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderRejectedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                              RestaurantServiceConfigData restaurantServiceConfigData,
                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel>
                                                     restaurantApprovalResponseAvroModelKafkaProducer,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.restaurantApprovalResponseAvroModelKafkaProducer = restaurantApprovalResponseAvroModelKafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderRejectedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();

        log.info("Received OrderRejectedEvent for order with id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper
                            .orderRejectedEventToRestaurantApprovalResponseAvroModel(domainEvent);
            restaurantApprovalResponseAvroModelKafkaProducer
                    .send(
                            restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            orderId,
                            restaurantApprovalResponseAvroModel,
                            kafkaMessageHelper.getKafkaCallback(
                                    restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                                    restaurantApprovalResponseAvroModel,
                                    orderId,
                                    "RestaurantApprovalResponseAvroModel"
                            )
                    );
            log.info("RestaurantApprovalResponseAvroModel is sent to Kafka for order id: {}",
                    restaurantApprovalResponseAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
                    " to Kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
