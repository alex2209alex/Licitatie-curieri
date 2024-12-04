ALTER TABLE users
    ALTER email_verification_code DROP NOT NULL,
    ALTER phone_verification_code DROP NOT NULL,
    ALTER verification_deadline DROP NOT NULL;