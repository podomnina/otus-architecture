CREATE TABLE IF NOT EXISTS menu.dish
(
    id serial PRIMARY KEY,
    name varchar(1024) NOT NULL,
    category varchar(120) NOT NULL,
    is_available bool NOT NULL default true,
    price numeric(8,2) not null default 0
);

CREATE TABLE IF NOT EXISTS menu.dish_product
(
    dish_id int references menu.dish (id) not null,
    product_id int NOT NULL,
    quantity int NOT NULL default 0,
    PRIMARY KEY(dish_id, product_id)
);
