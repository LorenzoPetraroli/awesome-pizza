CREATE INDEX idx_orders_status
    ON orders (status);

CREATE INDEX idx_orders_created_at
    ON orders (created_at);

CREATE INDEX idx_order_items_order_id
    ON order_items (order_id);

CREATE UNIQUE INDEX uk_orders_single_in_preparation
    ON orders ((status))
    WHERE status = 'IN_PREPARATION';