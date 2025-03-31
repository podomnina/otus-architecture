CREATE TABLE IF NOT EXISTS auth.identifier
(
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES auth.user (id) NOT NULL,
    login varchar(255) NOT NULL,
    blocked boolean,
    secret varchar(255) NOT NULL,
    CONSTRAINT identifier_login_unq UNIQUE (login)
);
COMMENT ON TABLE auth.identifier IS 'Сведения об аутентификации пользователей';
COMMENT ON COLUMN auth.identifier.id IS 'Идентификатор записи';
COMMENT ON COLUMN auth.identifier.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN auth.identifier.login IS 'Логин';
COMMENT ON COLUMN auth.identifier.blocked IS 'Признак блокировки';
COMMENT ON COLUMN auth.identifier.secret IS 'Криптографический ключ';
