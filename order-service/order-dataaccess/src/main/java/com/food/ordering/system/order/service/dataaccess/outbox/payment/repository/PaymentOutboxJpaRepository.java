package com.food.ordering.system.order.service.dataaccess.outbox.payment.repository;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {

    public Optional<List<PaymentOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                        OutboxStatus outboxStatus,
                                                                                        List<SagaStatus> sagaStatus);

    public Optional<PaymentOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type, UUID sagaId,
                                                                            List<SagaStatus> sagaStatus);

    public void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus,
                                                           List<SagaStatus> sagaStatus);
}
