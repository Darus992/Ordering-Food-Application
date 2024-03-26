CREATE TABLE restaurant
(
    restaurant_id                   SERIAL          NOT NULL,
    restaurant_image_card           BYTEA,
    restaurant_name                 VARCHAR(120)    NOT NULL,
    phone                           VARCHAR(120)    NOT NULL,
    email                           VARCHAR(120)    NOT NULL,
    food_menu_id                    INT,
    restaurant_address_id           INT             NOT NULL,
    restaurant_owner_id             INT             NOT NULL,
    restaurant_opening_time_id      INT             NOT NULL,
    PRIMARY KEY (restaurant_id),
    UNIQUE (email),
        FOREIGN KEY (food_menu_id)
            REFERENCES food_menu (food_menu_id),
        FOREIGN KEY (restaurant_address_id)
            REFERENCES address (address_id),
        FOREIGN KEY (restaurant_owner_id)
            REFERENCES restaurant_owner (restaurant_owner_id),
        FOREIGN KEY (restaurant_opening_time_id)
            REFERENCES restaurant_opening_time (restaurant_opening_time_id)
);