insert into "order".orders(id, customer_id, restaurant_id, tracking_id, price, order_status, failure_messages)
values ('9f818e0b-0746-4ec8-b365-87f2c024a2a3', 'ba81dc52-0e50-4298-a555-70fea73e65b1',
        '4559f42d-301d-4340-9945-81f3e95924e4',
        'e117e396-a0da-4193-9260-2f0f5b318eb2', 100.00, 'PENDING', '');

insert into "order".order_items(id, order_id, product_id, price, quantity, sub_total)
values (1, '9f818e0b-0746-4ec8-b365-87f2c024a2a3', '4782eb33-5a3e-421a-ae77-21c795669c95', 100.00, 1, 100.00);

insert into "order".order_address(id, order_id, street, postal_code, city)
values ('f2867608-a792-4edb-8138-1afdcca6847b', '9f818e0b-0746-4ec8-b365-87f2c024a2a3', 'test street', '1000AA',
        'test city');

insert into "order".payment_outbox(id, saga_id, created_at, type, payload, outbox_status, saga_status, order_status,
                                   version)
values ('dd06303f-6d2d-4104-8017-b2e383c752e3', '8edadde2-cf1c-49ad-82a5-d2e51585ba55', current_timestamp, 'OrderProcessingSaga',
        '{"price": 100, "orderId": "458a5eb4-6eea-4f3f-9c82-811aafd8eaf2", "createdAt": "2023-07-12T23:26:12.932543+03:30"},' ||
        '"customerId": "ba81dc52-0e50-4298-a555-70fea73e65b1:, "paymentOrderStatus": "PENDING"',
        'STARTED', 'STARTED', 'PENDING', 0);