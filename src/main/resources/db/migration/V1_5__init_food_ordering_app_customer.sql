CREATE TABLE customer
(
    customer_id     SERIAL       NOT NULL,
    name            VARCHAR(120) NOT NULL,
    surname         VARCHAR(120) NOT NULL,
    phone           VARCHAR(120) NOT NULL,
    address_id      INT          NOT NULL,
    PRIMARY KEY (customer_id),
    UNIQUE (phone),
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);