CREATE TABLE IF NOT EXISTS notification.notification
(
    id uuid PRIMARY KEY,
    send_to varchar not null,
    subject varchar not null,
    message varchar not null,
    created_at timestamptz not null default now()
);