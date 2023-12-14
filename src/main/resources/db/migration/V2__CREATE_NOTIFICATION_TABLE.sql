CREATE TABLE IF NOT EXISTS notification
(
    id                   SERIAL PRIMARY KEY NOT NULL,
    user_id_tg           BIGINT NOT NULL,
    morning_notification VARCHAR(16) NOT NULL DEFAULT '8',
    evening_notification varchar(16) NOT NULL DEFAULT '20',
    notification_on      BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id_tg) REFERENCES user_tg (user_id_tg)
);