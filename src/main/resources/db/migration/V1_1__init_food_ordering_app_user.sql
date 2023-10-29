CREATE TABLE users
(
    user_id     SERIAL          NOT NULL,
    user_name   VARCHAR(120)    NOT NULL,
    email       VARCHAR(140)    NOT NULL,
    password    VARCHAR(120)    NOT NULL,
    active      BOOLEAN         NOT NULL,
    user_role   VARCHAR(50)     NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (user_name, email)
);