CREATE TABLE orders
(
    order_id            SERIAL                      NOT NULL,
    order_number        INT                         NOT NULL,
    order_type          VARCHAR(13)                 NOT NULL,
    order_status        VARCHAR(11)                 NOT NULL,
    customer_comment    TEXT,
    received_date_time  TIMESTAMP WITH TIME ZONE    NOT NULL,
    completed_date_time TIMESTAMP WITH TIME ZONE    NOT NULL,
    customer_id         INT                         NOT NULL,
    user_id             INT                         NOT NULL,
    restaurant_id       INT                         NOT NULL,
    PRIMARY KEY (order_id),
    UNIQUE (order_number),
        FOREIGN KEY (customer_id)
            REFERENCES customer (customer_id),
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (restaurant_id)
);

CREATE TABLE orders_food
(
    orders_food_id  SERIAL NOT NULL,
    order_id                INT    NOT NULL,
    food_id                 INT    NOT NULL,
    quantity                INT    NOT NULL,
    PRIMARY KEY (orders_food_id),
        FOREIGN KEY (order_id)
            REFERENCES orders (order_id),
        FOREIGN KEY (food_id)
            REFERENCES food (food_id)
);