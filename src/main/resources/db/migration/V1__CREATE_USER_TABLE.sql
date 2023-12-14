CREATE TABLE IF NOT EXISTS user_tg
(
    user_id_tg        BIGINT PRIMARY KEY NOT NULL,
    chat_id           VARCHAR(128)       NOT NULL,
    user_tag          VARCHAR(128)       NOT NULL,
    registration_date TIMESTAMP          NOT NULL
);
