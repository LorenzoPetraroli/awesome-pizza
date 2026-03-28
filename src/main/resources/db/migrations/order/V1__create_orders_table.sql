CREATE TABLE orders (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ NULL,

    CONSTRAINT uk_orders_code UNIQUE (code),

    CONSTRAINT ck_orders_status
        CHECK (status IN ('PLACED', 'IN_PREPARATION', 'READY', 'COMPLETED'))
);
