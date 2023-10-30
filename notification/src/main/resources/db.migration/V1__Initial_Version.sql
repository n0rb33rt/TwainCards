
CREATE TABLE email_confirmation_token (
                        id BIGSERIAL,
                        token VARCHAR(255) NOT NULL,
                        confirmed BOOLEAN NOT NULL,
                        CONSTRAINT unique_token UNIQUE(token),
                        PRIMARY KEY(id)
);