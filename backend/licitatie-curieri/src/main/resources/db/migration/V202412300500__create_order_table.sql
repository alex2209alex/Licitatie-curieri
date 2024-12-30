CREATE SEQUENCE orders_seq INCREMENT 20 START 100;

CREATE TABLE orders
(
    id               BIGINT PRIMARY KEY,
    food_price       DECIMAL   NOT NULL,
    delivery_price   DECIMAL   NOT NULL,
    auction_deadline TIMESTAMP NOT NULL,
    order_status     TEXT      NOT NULL,
    client_id        BIGINT    NOT NULL REFERENCES users (id),
    courier_id       BIGINT REFERENCES users (id),
    address_id       BIGINT    NOT NULL REFERENCES addresses (id)
);

CREATE TABLE order_menu_item_associations
(
    order_id     BIGINT  NOT NULL REFERENCES orders (id),
    menu_item_id BIGINT  NOT NULL REFERENCES menu_items (id),
    quantity     INTEGER NOT NULL,
    PRIMARY KEY (order_id, menu_item_id)
);