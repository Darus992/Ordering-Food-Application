CREATE TABLE food_menu
(
    food_menu_id    SERIAL      NOT NULL,
    menu_name       VARCHAR(35) NOT NULL,
    PRIMARY KEY (food_menu_id)
);

CREATE TABLE food_menu_food
(
    food_menu_id    INT  NOT NULL,
    food_id         INT  NOT NULL,
    PRIMARY KEY (food_menu_id, food_id),
        FOREIGN KEY (food_menu_id)
            REFERENCES food_menu (food_menu_id),
        FOREIGN KEY (food_id)
            REFERENCES food (food_id)
);