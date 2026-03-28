CREATE TABLE order_items (
     id UUID PRIMARY KEY,
     order_id UUID NOT NULL,
     pizza_type VARCHAR(50) NOT NULL,
     quantity INTEGER NOT NULL,

     CONSTRAINT fk_order_items_order
         FOREIGN KEY (order_id)
             REFERENCES orders (id)
             ON DELETE CASCADE,

     CONSTRAINT ck_order_items_quantity
         CHECK (quantity > 0),

     CONSTRAINT ck_order_items_pizza_type
         CHECK (pizza_type IN ('MARGHERITA', 'DIAVOLA', 'CAPRICCIOSA', 'VEGETARIANA', 'QUATTRO_FORMAGGI'))
);
