package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception.RestaurantApprovalOutboxNotFoundException;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.RestaurantApprovalOutboxDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.RestaurantApprovalOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final RestaurantApprovalOutboxJpaRepository restaurantApprovalOutboxJpaRepository;
    private final RestaurantApprovalOutboxDataAccessMapper restaurantApprovalOutboxDataAccessMapper;

    public ApprovalOutboxRepositoryImpl(RestaurantApprovalOutboxJpaRepository
                                                restaurantApprovalOutboxJpaRepository,
                                        RestaurantApprovalOutboxDataAccessMapper
                                                restaurantApprovalOutboxDataAccessMapper) {
        this.restaurantApprovalOutboxJpaRepository = restaurantApprovalOutboxJpaRepository;
        this.restaurantApprovalOutboxDataAccessMapper = restaurantApprovalOutboxDataAccessMapper;
    }

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return restaurantApprovalOutboxDataAccessMapper.restaurantApprovalOutboxEntityToOrderApprovalOutboxMessage(
                restaurantApprovalOutboxJpaRepository.save(
                        restaurantApprovalOutboxDataAccessMapper
                                .OrderApprovalOutboxMessageToRestaurantApprovalOutboxEntity(orderApprovalOutboxMessage)
                )
        );
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                             OutboxStatus outboxStatus,
                                                                                             SagaStatus... sagaStatus) {
        return Optional.of(restaurantApprovalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                        outboxStatus,
                        Arrays.asList(sagaStatus))
                .orElseThrow(() -> new RestaurantApprovalOutboxNotFoundException("Restaurant approval outbox object " +
                        "could not be found for saga type: " + type))
                .stream()
                .map(restaurantApprovalOutboxDataAccessMapper
                        ::restaurantApprovalOutboxEntityToOrderApprovalOutboxMessage).toList()
        );
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId,
                                                                                 SagaStatus... sagaStatus) {
        return restaurantApprovalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type,
                sagaId,
                Arrays.asList(sagaStatus)
        ).map(restaurantApprovalOutboxDataAccessMapper::restaurantApprovalOutboxEntityToOrderApprovalOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
                                                         SagaStatus... sagaStatus) {
        restaurantApprovalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
                type,
                outboxStatus,
                Arrays.asList(sagaStatus)
        );
    }
}
