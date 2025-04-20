CREATE TABLE IF NOT EXISTS payment.account
(
    user_id uuid PRIMARY KEY,
    amount numeric(10, 2) not null default 0,
    created_at timestamptz not null default now()
);

CREATE TABLE IF NOT EXISTS payment.payment
(
    id uuid PRIMARY KEY,
    order_id uuid not null,
    account_id uuid REFERENCES payment.account (user_id) NOT NULL,
    amount numeric(8, 2) not null default 0,
    status varchar not null,
    method varchar not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz
);

CREATE TABLE IF NOT EXISTS payment.refund
(
    id uuid PRIMARY KEY,
    payment_id uuid REFERENCES payment.payment (id) NOT NULL,
    amount numeric(8, 2) not null default 0,
    status varchar not null,
    transaction_id varchar not null,
    created_at timestamptz not null default now()
);

