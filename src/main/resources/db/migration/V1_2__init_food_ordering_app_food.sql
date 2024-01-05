CREATE TABLE food
(
    food_id     SERIAL          NOT NULL,
    category    VARCHAR(35)     NOT NULL,
    name        VARCHAR(35)     NOT NULL,
    description TEXT,
    price       NUMERIC(6,2)    NOT NULL,
    PRIMARY KEY (food_id)
);