CREATE TABLE restaurant
(
    restaurant_id                   SERIAL          NOT NULL,
    restaurant_name                 VARCHAR(120)    NOT NULL,
    phone                           VARCHAR(120)    NOT NULL,
    email                           VARCHAR(120)    NOT NULL,
    food_menu_id                    INT,
    restaurant_address_id           INT             NOT NULL,
--    restaurant_owner_id             INT             NOT NULL,
    user_id                         INT             NOT NULL,
    schedule_id                     INT,
    delivery_address_id             INT,
    PRIMARY KEY (restaurant_id),
    UNIQUE (email),
        FOREIGN KEY (food_menu_id)
            REFERENCES food_menu (food_menu_id),
        FOREIGN KEY (restaurant_address_id)
            REFERENCES address (address_id),
--        FOREIGN KEY (restaurant_owner_id)
--            REFERENCES restaurant_owner (restaurant_owner_id),
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
        FOREIGN KEY (schedule_id)
            REFERENCES schedule (schedule_id),
        FOREIGN KEY (delivery_address_id)
            REFERENCES address (address_id)
);