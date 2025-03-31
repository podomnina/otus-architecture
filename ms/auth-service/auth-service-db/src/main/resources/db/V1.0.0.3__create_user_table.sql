CREATE TABLE IF NOT EXISTS auth.user
(
    id uuid PRIMARY KEY,
    last_name varchar(1024),
    first_name varchar(1024),
    second_name varchar(1024),
    email varchar(1024),
    created_at timestamp with time zone NOT NULL,
    blocked_at timestamp with time zone,
    restaurant_id uuid REFERENCES auth.restaurant (id) NOT NULL
);
COMMENT ON TABLE auth.user IS 'Информация о пользователях системы';
COMMENT ON COLUMN auth.user.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN auth.user.last_name IS 'Фамилия';
COMMENT ON COLUMN auth.user.first_name IS 'Имя';
COMMENT ON COLUMN auth.user.second_name IS 'Отчество';
COMMENT ON COLUMN auth.user.email IS 'Электронный адрес пользователя';
COMMENT ON COLUMN auth.user.created_at IS 'Время создания пользователя';
COMMENT ON COLUMN auth.user.blocked_at IS 'Время блокировки';
COMMENT ON COLUMN auth.user.restaurant_id IS 'Идентификатор ресторана';