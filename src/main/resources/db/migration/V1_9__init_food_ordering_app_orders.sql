CREATE TABLE orders
(
    order_id                        SERIAL                      NOT NULL,
    order_number                    INT                         NOT NULL,
    order_status                    VARCHAR(11),
    order_notes                     TEXT,
    received_date_time              TIMESTAMP WITH TIME ZONE,
    completed_date_time             TIMESTAMP WITH TIME ZONE,
    total_price                     NUMERIC(6,2),
    customer_id                     INT                         NOT NULL,
    restaurant_id                   INT                         NOT NULL,
    PRIMARY KEY (order_id),
    UNIQUE (order_number),
        FOREIGN KEY (customer_id)
            REFERENCES customer (customer_id),
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (restaurant_id)
);

CREATE TABLE order_foods (
    order_id    INT NOT NULL,
    food_id     INT NOT NULL,
    quantity    INT NOT NULL,
    PRIMARY KEY (order_id, food_id),
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    FOREIGN KEY (food_id) REFERENCES food (food_id)
);