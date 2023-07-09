package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper restaurantApprovalOutboxDataAccessMapper;

    public ApprovalOutboxRepositoryImpl(ApprovalOutboxJpaRepository
                                                approvalOutboxJpaRepository,
                                        ApprovalOutboxDataAccessMapper
                                                restaurantApprovalOutboxDataAccessMapper) {
        this.approvalOutboxJpaRepository = approvalOutboxJpaRepository;
        this.restaurantApprovalOutboxDataAccessMapper = restaurantApprovalOutboxDataAccessMapper;
    }

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return restaurantApprovalOutboxDataAccessMapper.approvalOutboxEntityToOrderApprovalOutboxMessage(
                approvalOutboxJpaRepository.save(
                        restaurantApprovalOutboxDataAccessMapper
                                .OrderApprovalOutboxMessageToApprovalOutboxEntity(orderApprovalOutboxMessage)
                )
        );
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                             OutboxStatus outboxStatus,
                                                                                             SagaStatus... sagaStatus) {
        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                        outboxStatus,
                        Arrays.asList(sagaStatus))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object " +
                        "could not be found for saga type: " + type))
                .stream()
                .map(restaurantApprovalOutboxDataAccessMapper
                        ::approvalOutboxEntityToOrderApprovalOutboxMessage).toList()
        );
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId,
                                                                                 SagaStatus... sagaStatus) {
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type,
                sagaId,
                Arrays.asList(sagaStatus)
        ).map(restaurantApprovalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
                                                         SagaStatus... sagaStatus) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
                type,
                outboxStatus,
                Arrays.asList(sagaStatus)
        );
    }
}
