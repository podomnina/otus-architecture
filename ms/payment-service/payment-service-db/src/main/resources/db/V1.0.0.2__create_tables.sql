CREATE TABLE IF NOT EXISTS payment.payment
(
    id uuid PRIMARY KEY,
    order_id uuid not null,
    user_id uuid not null,
    amount int not null default 0,
    status varchar not null,
    method varchar not null,
    transaction_id varchar not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz
);

CREATE TABLE IF NOT EXISTS payment.refund
(
    id uuid PRIMARY KEY,
    payment_id uuid REFERENCES payment.payment (id) NOT NULL,
    amount int not null default 0,
    status varchar not null,
    transaction_id varchar not null,
    created_at timestamptz not null default now()
);

