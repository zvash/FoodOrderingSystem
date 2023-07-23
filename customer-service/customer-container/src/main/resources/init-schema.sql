DROP SCHEMA IF EXISTS "customer" CASCADE;

CREATE SCHEMA "customer";

CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "customer".customers
(
    id         uuid NOT NULL,
    username   character varying COLLATE pg_catalog."default",
    first_name character varying COLLATE pg_catalog."default",
    last_name  character varying COLLATE pg_catalog."default",
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS customer.order_customer_m_view;
CREATE MATERIALIZED VIEW customer.order_customer_m_view
    TABLESPACE pg_default
AS
SELECT id,
       username,
       first_name,
       last_name
FROM customer.customers
WITH DATA;

refresh materialized view customer.order_customer_m_view;

DROP function IF EXISTS customer.refresh_order_customer_m_view;

CREATE OR REPLACE function customer.refresh_order_customer_m_view()
    returns trigger
AS
'
    BEGIN
        refresh materialized view customer.order_customer_m_view;
        return null;
    END;
' LANGUAGE plpgsql;

DROP trigger IF EXISTS order_customer_m_view ON customer.customers;

CREATE trigger order_customer_m_view
    after INSERt OR UPDATE OR DELETE OR TRUNCATE
    ON customer.customers
    FOR EACH statement
EXECUTE procedure customer.refresh_order_customer_m_view();