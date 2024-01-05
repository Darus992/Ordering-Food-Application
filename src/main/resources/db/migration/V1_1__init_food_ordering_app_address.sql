CREATE TABLE address
(
    address_id              SERIAL      NOT NULL,
    city                    VARCHAR(32) NOT NULL,
    district                VARCHAR(32) NOT NULL,
    postal_code             VARCHAR(32) NOT NULL,
    address                 VARCHAR(50) NOT NULL,
    PRIMARY KEY (address_id)
);