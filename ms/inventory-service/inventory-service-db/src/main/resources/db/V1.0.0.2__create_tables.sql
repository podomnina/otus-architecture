CREATE TABLE IF NOT EXISTS inventory.product
(
    id uuid PRIMARY KEY,
    name varchar(1024) NOT NULL
);

CREATE TABLE IF NOT EXISTS inventory.inventory
(
    product_id uuid REFERENCES inventory.product (id) NOT NULL,
    quantity int NOT NULL default 0,
    threshold int not null default 0,
    CONSTRAINT inventory_product_unq UNIQUE (product_id)
);

CREATE TABLE IF NOT EXISTS inventory.reserved_product
(
    order_id uuid NOT NULL,
    product_id uuid REFERENCES inventory.product (id) NOT NULL,
    quantity int NOT NULL default 0,
    PRIMARY KEY(order_id, product_id)
);
