CREATE TABLE movies (
    movie_id           BIGINT                  NOT NULL AUTO_INCREMENT,
    title              VARCHAR(255)            NOT NULL,
    description        CHARACTER VARYING(1000) DEFAULT NULL,
    duration           VARCHAR(25)             NOT NULL,
    rating             INT                     DEFAULT NULL,
    age_limit          INT                     DEFAULT NULL,
    language_translate VARCHAR(50)             DEFAULT NULL,
    created            TIMESTAMP               DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (movie_id),
    UNIQUE (title)
);

CREATE TABLE orders (
    order_id         BIGINT       NOT NULL AUTO_INCREMENT,
    movie_id         BIGINT       NOT NULL,
    price            NUMERIC(6,2) NOT NULL,
    hall             VARCHAR(50)  DEFAULT NULL,
    customer_name    VARCHAR(100) DEFAULT NULL,
    customer_age     INT          DEFAULT NULL,
    additional       VARCHAR(50)  DEFAULT NULL,
    date_session     TIMESTAMP    NOT NULL,
    percent_discount INT          DEFAULT NULL,
    is_birthday      BOOLEAN      DEFAULT NULL,
    created          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (order_id),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id) ON DELETE CASCADE
);

INSERT INTO PUBLIC.MOVIES (TITLE, DESCRIPTION, DURATION, RATING, AGE_LIMIT, LANGUAGE_TRANSLATE)
VALUES ('Shrek 1', 'cool cartoon', '1h 30m', 9, 12, 'UA'),
       ('Shrek 2', 'super cool cartoon', '1h 40m', 8, 14, 'UA');

INSERT INTO PUBLIC.ORDERS (MOVIE_ID, PRICE, HALL, CUSTOMER_NAME, CUSTOMER_AGE, ADDITIONAL, DATE_SESSION, PERCENT_DISCOUNT, IS_BIRTHDAY)
VALUES (1, 42.00,'A', 'Bob', 17, 'cola', '2022-10-08 04:00:00.000000', null, false),
       (2, 32.00,'B', 'John', 13, 'popcorn, cola', '2022-10-07 04:30:00.000000', null, false),
       (1, 22.00,'A','Peter', 27, 'cola', '2022-10-11 04:30:00.000000', null, false),
       (2, 15.00,'C','Alla', 27, 'popcorn', '2022-10-08 04:00:00.000000', null, false),
       (1, 32.00,'A','John', 17, 'cola', '2022-10-07 04:30:00.000000', 4.00, true),
       (2, 42.00,'C','Sven', 27, 'popcorn, cola', '2022-10-12 04:00:00.000000', null, false),
       (1, 12.00,'A','Maria', 14, 'cola', '2022-10-07 04:30:00.000000', null, false),
       (2, 42.00,'B','Sveta', 27, 'popcorn', '2022-10-06 04:00:00.000000', null, false),
       (1, 32.00,'B','John', 12, 'cola', '2022-10-07 04:30:00.000000', null, false),
       (2, 42.00,'A','Mykola', 11, 'popcorn, cola', '2022-10-11 04:30:00.000000', null, false),
       (1, 15.00,'B','Georg', 14, 'cola', '2022-10-10 04:00:00.000000', null, false),
       (2, 36.00,'C','John', 16, 'popcorn', '2022-10-07 04:30:00.000000', null, false);