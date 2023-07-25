package com.food.ordering.system.customer.service.messaging.publisher.kafka;

import com.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import com.food.ordering.system.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class CustomerCreatedEventKafkaPublisher implements CustomerMessagePublisher {

    private final CustomerServiceConfigData customerServiceConfigData;
    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;

    public CustomerCreatedEventKafkaPublisher(CustomerServiceConfigData customerServiceConfigData,
                                              CustomerMessagingDataMapper customerMessagingDataMapper,
                                              KafkaProducer<String, CustomerAvroModel> kafkaProducer) {
        this.customerServiceConfigData = customerServiceConfigData;
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(CustomerCreatedEvent domainEvent) {
        log.info("Received CustomerCreatedEvent for customer id: {}",
                domainEvent.getCustomer().getId().getValue().toString());
        try {
            CustomerAvroModel customerAvroModel = customerMessagingDataMapper
                    .customerCreatedEventToCustomerAvroModel(domainEvent);
            kafkaProducer.send(
                    customerServiceConfigData.getCustomerTopicName(),
                    customerAvroModel.getId(),
                    customerAvroModel,
                    callback(customerServiceConfigData.getCustomerTopicName(), customerAvroModel)
            );
            log.info("CustomerCreatedEvent sent to kafka for customer id: {}", customerAvroModel.getId());
        } catch (Exception e) {
            log.error("Error while sending CustomerCreatedEvent to kafka for customer id: {}, error: {}",
                    domainEvent.getCustomer().getId().getValue().toString(), e.getMessage());
        }
    }

    private ListenableFutureCallback<SendResult<String, CustomerAvroModel>>
    callback(String topicName, CustomerAvroModel customerAvroModel) {
        return new ListenableFutureCallback<SendResult<String, CustomerAvroModel>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending message: {} to topic: {}", customerAvroModel.toString(), topicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, CustomerAvroModel> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for customer id: {} " +
                                "Topic: {} Partition: {} Offset: {} Timestamp: {}, at time: {}",
                        customerAvroModel.getId(),
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
        };
    }
}
