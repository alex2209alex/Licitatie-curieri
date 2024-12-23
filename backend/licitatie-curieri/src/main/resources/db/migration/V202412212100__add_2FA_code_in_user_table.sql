ALTER TABLE users
    ADD COLUMN two_fa_code             VARCHAR(255)  NULL,
    ADD COLUMN verify_fa_code_deadline TIMESTAMP     NULL;