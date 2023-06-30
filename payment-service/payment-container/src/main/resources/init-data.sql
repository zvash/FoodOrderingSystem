INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
VALUES ('431065c8-39c1-4f50-9965-857b4c2b416d', 'b2834be1-6ae8-453b-a120-17adb605bd73', 500.00);
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('983e9579-6751-4985-b3a9-aefc99966124', 'b2834be1-6ae8-453b-a120-17adb605bd73', 100.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('b60c159c-09d5-446f-a124-c95bf3c64a4c', 'b2834be1-6ae8-453b-a120-17adb605bd73', 600.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('adabb470-a66d-47c5-bf0c-470dae5470ac', 'b2834be1-6ae8-453b-a120-17adb605bd73', 200.00, 'DEBIT');

INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
VALUES ('cd7426ca-a6ae-4ce2-8d03-3ca7ccde8939', 'b029ff40-4827-45b1-925d-c22e9c3569d4', 100.00);
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('633830e8-4b9d-4e8a-bff3-476a35d214fc', 'b029ff40-4827-45b1-925d-c22e9c3569d4', 100.00, 'CREDIT');