CREATE TABLE restaurant_opening_time
(
    restaurant_opening_time_id  SERIAL  NOT NULL,
    opening_hour                TIME WITHOUT TIME ZONE,
    close_hour                  TIME WITHOUT TIME ZONE,
    day_of_week_from            VARCHAR(35)  NOT NULL,
    day_of_week_till            VARCHAR(35)  NOT NULL,
    PRIMARY KEY (restaurant_opening_time_id)
);