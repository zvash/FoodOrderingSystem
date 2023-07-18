package com.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderOutboxScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;

    public OrderOutboxScheduler(OrderOutboxHelper orderOutboxHelper,
                                RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher) {
        this.orderOutboxHelper = orderOutboxHelper;
        this.restaurantApprovalResponseMessagePublisher = restaurantApprovalResponseMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${restaurant-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${restaurant-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessagesByOutboxStatus(OutboxStatus.STARTED);
        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage with  ids: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            outboxMessage.getId().toString()).collect(Collectors.joining(",")));
            outboxMessages.forEach(outboxMessage ->
                    restaurantApprovalResponseMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
            log.info("{} OrderOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

    private void updateOutboxStatus(OrderOutboxMessage outboxMessage, OutboxStatus outboxStatus) {
        outboxMessage.setOutboxStatus(outboxStatus);
        orderOutboxHelper.save(outboxMessage);
        log.info("OrderOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
