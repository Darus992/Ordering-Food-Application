CREATE TABLE users
(
    user_id     SERIAL          NOT NULL,
    user_name   VARCHAR(120)    NOT NULL,
    email       VARCHAR(140)    NOT NULL,
    password    VARCHAR(120)    NOT NULL,
    active      BOOLEAN         NOT NULL,
    owner_id    INT,
    customer_id INT,
    PRIMARY KEY (user_id),
    UNIQUE (user_name, email),
        FOREIGN KEY (owner_id)
            REFERENCES restaurant_owner (restaurant_owner_id),
        FOREIGN KEY (customer_id)
            REFERENCES customer (customer_id)
);

CREATE TABLE user_roles
(
    user_id         INT,
    role_name       VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);