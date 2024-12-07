CREATE SEQUENCE addresses_seq INCREMENT 20 START 100;

CREATE TABLE addresses
(
    id        BIGINT PRIMARY KEY,
    details   TEXT    NOT NULL,
    latitude  DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL
);

CREATE TABLE user_address_associations
(
    user_id    BIGINT NOT NULL REFERENCES users (id),
    address_id BIGINT NOT NULL REFERENCES addresses (id),
    PRIMARY KEY (user_id, address_id)
);

CREATE SEQUENCE restaurants_seq INCREMENT 20 START 100;

CREATE TABLE restaurants
(
    id         BIGINT PRIMARY KEY,
    name       TEXT   NOT NULL,
    address_id BIGINT NOT NULL references addresses (id)
);
