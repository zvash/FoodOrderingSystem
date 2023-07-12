package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = OrderServiceApplication.class)
@Sql(value = {"classpath:sql/OrderPaymentSagaTestSetUp.sql"})
@Sql(value = {"classpath:sql/OrderPaymentSagaTestCleanUp.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderPaymentSagaTest {

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    private final UUID SAGA_ID = UUID.fromString("8edadde2-cf1c-49ad-82a5-d2e51585ba55");
    private final UUID ORDER_ID = UUID.fromString("9f818e0b-0746-4ec8-b365-87f2c024a2a3");
    private final UUID CUSTOMER_ID = UUID.fromString("ba81dc52-0e50-4298-a555-70fea73e65b1");
    private final UUID PAYMENT_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("100");

    @Test
    void testDoublePayment() {
        orderPaymentSaga.process(getPaymentResponse());
        orderPaymentSaga.process(getPaymentResponse());
    }

    @Test
    void testDoublePaymentWithThreads() throws InterruptedException {
        Thread thread1 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
        Thread thread2 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertPaymentOutbox();
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(SAGA_ID.toString())
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentId(PAYMENT_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }

    private void assertPaymentOutbox() {
        Optional<PaymentOutboxEntity> paymentOutboxEntity =
                paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(ORDER_SAGA_NAME, SAGA_ID,
                        List.of(SagaStatus.PROCESSING));
        assertTrue(paymentOutboxEntity.isPresent());
    }
}
