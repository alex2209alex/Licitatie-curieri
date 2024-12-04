CREATE TABLE users
(
    id                      BIGINT PRIMARY KEY,
    first_name              TEXT      NOT NULL,
    last_name               TEXT      NOT NULL,
    email                   TEXT      NOT NULL,
    phone_number            TEXT      NOT NULL,
    password                TEXT      NOT NULL,
    user_type               TEXT      NOT NULL,
    email_verification_code TEXT      NOT NULL,
    phone_verification_code TEXT      NOT NULL,
    verification_deadline   TIMESTAMP NOT NULL,
    UNIQUE (email, user_type)
);