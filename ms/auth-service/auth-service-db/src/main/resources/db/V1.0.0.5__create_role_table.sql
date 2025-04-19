CREATE TABLE IF NOT EXISTS auth.role
(
    id uuid PRIMARY KEY,
    name varchar(255) NOT NULL,
    CONSTRAINT role_unq UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS auth.user_role
(
    user_id uuid REFERENCES auth.user (id) NOT NULL,
    role_id uuid REFERENCES auth.role (id) NOT NULL,
    PRIMARY KEY (user_id, role_id)
);