CREATE TABLE IF NOT EXISTS delivery.delivery_man
(
    id serial PRIMARY KEY,
    name varchar not null
);

CREATE TABLE IF NOT EXISTS delivery.delivery
(
    id serial PRIMARY KEY,
    order_id int not null,
    status varchar not null,
    timeslot int not null,
    delivery_man_id int REFERENCES delivery.delivery_man (id) NOT NULL,
    created_at timestamptz not null default now(),
    completed_at timestamptz
);