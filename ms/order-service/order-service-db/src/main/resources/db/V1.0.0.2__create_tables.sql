CREATE TABLE IF NOT EXISTS order.order
(
    id uuid PRIMARY KEY,
    user_id uuid not null,
    status varchar NOT NULL,
    total_price int not null,
    created_at timestamptz not null default now(),
    completed_at timestamptz
);

CREATE TABLE IF NOT EXISTS order.order_item
(
    order_id uuid REFERENCES order.order (id) NOT NULL,
    dish_id uuid not null,
    price int not null default 0,
    quantity int NOT NULL default 0,
    PRIMARY KEY(order_id, dish_id)
);