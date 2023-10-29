CREATE TABLE schedule
(
    schedule_id SERIAL NOT NULL,
    PRIMARY KEY (schedule_id)
);

CREATE TABLE schedule_hours
(
    schedule_id                 INT  NOT NULL,
    restaurant_opening_time_id  INT  NOT NULL,
    PRIMARY KEY (schedule_id, restaurant_opening_time_id),
        FOREIGN KEY (schedule_id)
            REFERENCES schedule (schedule_id),
        FOREIGN KEY (restaurant_opening_time_id)
            REFERENCES restaurant_opening_time (restaurant_opening_time_id)
);