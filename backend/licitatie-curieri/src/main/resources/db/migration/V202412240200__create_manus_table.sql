CREATE TABLE menus
(
    id               BIGINT        PRIMARY KEY,
    name             TEXT          NOT NULL,
    price            DECIMAL(5, 2) NOT NULL,
    ingredients_list TEXT          NOT NULL,
    photo            TEXT          NOT NULL,
    discount         DECIMAL(5, 2) NOT NULL
);