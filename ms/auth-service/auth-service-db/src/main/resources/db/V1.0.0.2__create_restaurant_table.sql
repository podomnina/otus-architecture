CREATE TABLE IF NOT EXISTS auth.restaurant
(
    id uuid PRIMARY KEY,
    name varchar(1024) NOT NULL,
    code varchar(6) NOT NULL
);
COMMENT ON TABLE auth.restaurant IS 'Информация о ресторане';
COMMENT ON COLUMN auth.restaurant.id IS 'Идентификатор ресторана';
COMMENT ON COLUMN auth.restaurant.name IS 'Наименование ресторана';
COMMENT ON COLUMN auth.restaurant.code IS 'Код ресторана';