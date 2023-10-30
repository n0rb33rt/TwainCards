

CREATE TABLE "user" (
    id BIGSERIAL,
    email VARCHAR(319) NOT NULL,
    password VARCHAR(61),
    role VARCHAR(8) NOT NULL,
    enabled BOOLEAN NOT NULL,
    set_password BOOLEAN NOT NULL,
    subscription_until TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT unique_email UNIQUE(email),
    CONSTRAINT check_email CHECK(email ~ '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$'),
    CONSTRAINT check_role CHECK ( role IN ('USER','PREMIUM') ),
    PRIMARY KEY(id)
);

CREATE TABLE jwt_token (
    id BIGSERIAL,
    token VARCHAR(196) NOT NULL,
    revoked BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL ,
    CONSTRAINT unique_token UNIQUE(token),
    CONSTRAINT user_id_fk FOREIGN KEY(user_id) REFERENCES "user"(id),
    PRIMARY KEY (id)
);
CREATE TABLE deck(
                     id BIGSERIAL,
                     native_language VARCHAR(13) NOT NULL,
                     learning_language VARCHAR(13) NOT NULL,
                     total_words INT NOT NULL,
                     words_to_learn INT NOT NULL,
                     known_words INT NOT NULL ,
                     learned_words INT NOT NULL,
                     user_id INT NOT NULL,
                     CONSTRAINT user_id_fk FOREIGN KEY(user_id) REFERENCES "user"(id),
                     CONSTRAINT check_native_language CHECK ( native_language IN ('ENGLISH','UKRAINIAN') ),
                     CONSTRAINT check_learning_language CHECK ( learning_language IN ('ENGLISH','UKRAINIAN') ),
                     PRIMARY KEY (id)
);
CREATE TABLE card(
    id BIGSERIAL,
    native_word VARCHAR(50) NOT NULL ,
    translation VARCHAR(50) NOT NULL ,
    description VARCHAR(150),
    state VARCHAR(13) NOT NULL,
    deck_id BIGINT NOT NULL ,
    CONSTRAINT check_state CHECK ( state IN ('TO_LEARN','KNOW','HAVE_LEARNED')),
    CONSTRAINT deck_id_fk FOREIGN KEY(deck_id) REFERENCES deck(id),
    PRIMARY KEY(id)
);

