CREATE TYPE event_status_enum AS ENUM ('FINISHED', 'CONFIRMED', 'CANCELLED', 'IN_PLANNING', 'READY');

-- Table: users
CREATE TABLE users
(
    id_user              serial       NOT NULL,
    email                varchar(100) NOT NULL,
    hashed_password      varchar(300) NOT NULL,
    user_type            char         NOT NULL,
    reset_password_token varchar(400) NULL,
    CONSTRAINT users_pk PRIMARY KEY (id_user)
);

-- tables
-- Table: address
CREATE TABLE address
(
    id_address    serial      NOT NULL,
    country       varchar(30) NOT NULL,
    street_name   varchar(50) NOT NULL,
    street_number int         NOT NULL,
    zip_code      varchar(10) NOT NULL,
    city          varchar(30) NOT NULL,
    CONSTRAINT address_pk PRIMARY KEY (id_address)
);

-- Table: admin
CREATE TABLE admin
(
    id_admin_user int         NOT NULL,
    access        varchar(50) NOT NULL,
    CONSTRAINT admin_pk PRIMARY KEY (id_admin_user)
);

-- Table: business
CREATE TABLE business
(
    id_business_user    int         NOT NULL,
    first_name          varchar(30) NOT NULL,
    last_name           varchar(40) NOT NULL,
    business_name       varchar(40) NULL,
    phone_number        bigint      NOT NULL,
    verification_status varchar(30) NOT NULL,
    id_business_address int         NOT NULL,
    CONSTRAINT business_pk PRIMARY KEY (id_business_user)
);

-- Table: business_app_problem
CREATE TABLE app_problem
(
    id_app_problem serial       NOT NULL,
    date_time      timestamp    NOT NULL,
    concern        varchar(100) NOT NULL,
    description    varchar(500) NOT NULL,
    id_user        int          NOT NULL,
    CONSTRAINT app_problem_pk PRIMARY KEY (id_app_problem)
);

-- Table: catering
CREATE TABLE catering
(
    id_catering         serial       NOT NULL,
    name                varchar(100) NOT NULL,
    email               varchar(100) NOT NULL,
    phone_number        bigint       NOT NULL,
    service_cost        decimal(8, 2) NULL,
    description         varchar(500) NULL,
    id_business         int          NOT NULL,
    id_catering_address int          NOT NULL,
    CONSTRAINT catering_pk PRIMARY KEY (id_catering)
);

-- Table: catering_image
CREATE TABLE catering_image
(
    id_catering_image serial       NOT NULL,
    image             varchar(300) NOT NULL,
    alt               varchar(50)  NOT NULL,
    id_catering       int          NOT NULL,
    CONSTRAINT catering_image_pk PRIMARY KEY (id_catering_image)
);

-- Table: catering_item
CREATE TABLE catering_item
(
    id_catering_item serial        NOT NULL,
    name             varchar(50)   NOT NULL,
    type             varchar(10)   NOT NULL,
    description      varchar(200) NULL,
    serving_price    decimal(6, 2) NOT NULL,
    is_vegan         boolean       NOT NULL,
    is_vegetarian    boolean       NOT NULL,
    is_gluten_free   boolean       NOT NULL,
    id_catering      int           NOT NULL,
    CONSTRAINT catering_item_pk PRIMARY KEY (id_catering_item)
);

-- Table: catering_location
CREATE TABLE catering_location
(
    id_catering int NOT NULL,
    id_location int NOT NULL,
    CONSTRAINT catering_location_pk PRIMARY KEY (id_catering, id_location)
);

-- Table: catering_review
CREATE TABLE catering_review
(
    id_catering_review serial       NOT NULL,
    title              varchar(100) NOT NULL,
    comment            varchar(300) NOT NULL,
    id_catering        int          NOT NULL,
    star_review        int          NOT NULL,
    id_customer        int          NOT NULL,
    CONSTRAINT catering_review_pk PRIMARY KEY (id_catering_review)
);

-- Table: customer
CREATE TABLE customer
(
    id_customer_user    int         NOT NULL,
    first_name          varchar(30) NOT NULL,
    last_name           varchar(40) NOT NULL,
    birthdate           date        NOT NULL,
    phone_number        bigint      NOT NULL,
    id_customer_address int         NOT NULL,
    CONSTRAINT customer_pk PRIMARY KEY (id_customer_user)
);

-- Table: description_item
CREATE TABLE description_item
(
    name        varchar(40) NOT NULL,
    description varchar(100) NULL,
    CONSTRAINT description_item_pk PRIMARY KEY (name)
);

-- Table: event_type
CREATE TABLE event_type
(
    id_event_type serial      NOT NULL,
    type          varchar(30) NOT NULL,
    description   varchar(200) NULL,
    CONSTRAINT event_type_pk PRIMARY KEY (id_event_type)
);

-- Table: guest
CREATE TABLE guest
(
    id_guest    serial       NOT NULL,
    first_name  varchar(30)  NOT NULL,
    last_name   varchar(40)  NOT NULL,
    email       varchar(100) NOT NULL,
    id_customer int          NOT NULL,
    CONSTRAINT guest_pk PRIMARY KEY (id_guest)
);

-- Table: location
CREATE TABLE location
(
    id_location         serial        NOT NULL,
    name                varchar(50)   NOT NULL,
    email               varchar(100)  NOT NULL,
    phone_number        bigint        NOT NULL,
    seating_capacity    int           NOT NULL,
    standing_capacity   int           NOT NULL,
    description         varchar(300) NULL,
    daily_rent_cost     decimal(8, 2) NOT NULL,
    size_in_sq_meters   int NULL,
    id_business         int           NOT NULL,
    id_location_address int           NOT NULL,
    CONSTRAINT location_pk PRIMARY KEY (id_location)
);

-- Table: location_description
CREATE TABLE location_description
(
    id_location           int         NOT NULL,
    description_item_name varchar(40) NOT NULL,
    CONSTRAINT location_description_pk PRIMARY KEY (id_location, description_item_name)
);

-- Table: location_for_event
CREATE TABLE location_for_event
(
    id_location_for_event serial    NOT NULL,
    time_from             timestamp NOT NULL,
    time_to               timestamp NULL,
    id_organized_event    int       NOT NULL,
    id_location           int       NOT NULL,
    CONSTRAINT location_for_event_pk PRIMARY KEY (id_location_for_event)
);

-- Table: location_image
CREATE TABLE location_image
(
    id_location_image serial       NOT NULL,
    image             varchar(300) NOT NULL,
    alt               varchar(50)  NOT NULL,
    id_location       int          NOT NULL,
    CONSTRAINT location_image_pk PRIMARY KEY (id_location_image)
);

-- Table: location_review
CREATE TABLE location_review
(
    id_location_review serial       NOT NULL,
    title              varchar(100) NOT NULL,
    comment            varchar(300) NOT NULL,
    id_location        int          NOT NULL,
    id_customer        int          NOT NULL,
    CONSTRAINT location_review_pk PRIMARY KEY (id_location_review)
);

-- Table: optional_service
CREATE TABLE optional_service
(
    id_optional_service serial        NOT NULL,
    "alias"             varchar(50) NULL,
    type                varchar(50)   NOT NULL,
    email               varchar(100)  NOT NULL,
    description         varchar(300)  NOT NULL,
    service_cost        decimal(8, 2) NOT NULL,
    id_business         int           NOT NULL,
    CONSTRAINT optional_service_pk PRIMARY KEY (id_optional_service)
);

-- Table: optional_service_image
CREATE TABLE optional_service_image
(
    id_optional_service_image serial       NOT NULL,
    image                     varchar(300) NOT NULL,
    alt                       varchar(50)  NOT NULL,
    id_optional_service       int          NOT NULL,
    CONSTRAINT optional_service_image_pk PRIMARY KEY (id_optional_service_image)
);

-- Table: organized_event
CREATE TABLE organized_event
(
    id_organized_event serial            NOT NULL,
    start_date         date              NOT NULL,
    end_date           date NULL,
    is_predefined      boolean           NOT NULL,
    event_status       event_status_enum NOT NULL,
    id_event_type      int               NOT NULL,
    id_customer        int               NOT NULL,
    CONSTRAINT organized_event_pk PRIMARY KEY (id_organized_event)
);

-- Table: organized_event_guest
CREATE TABLE organized_event_guest
(
    id_guest           int NOT NULL,
    id_organized_event int NOT NULL,
    CONSTRAINT organized_event_guest_pk PRIMARY KEY (id_guest, id_organized_event)
);

-- Table: service_for_event
CREATE TABLE service_for_event
(
    id_chosen_service_for_event serial       NOT NULL,
    time_from                   timestamp    NOT NULL,
    time_to                     timestamp    NOT NULL,
    comment                     varchar(200) NOT NULL,
    id_optional_service         int          NOT NULL,
    id_location_for_event       int          NOT NULL,
    CONSTRAINT service_for_event_pk PRIMARY KEY (id_chosen_service_for_event)
);

-- Table: service_review
CREATE TABLE service_review
(
    id_service_review   serial       NOT NULL,
    title               varchar(100) NOT NULL,
    comment             varchar(300) NOT NULL,
    id_optional_service int          NOT NULL,
    id_customer         int          NOT NULL,
    CONSTRAINT service_review_pk PRIMARY KEY (id_service_review)
);

-- foreign keys
-- Reference: admin_user (table: admin)
ALTER TABLE admin
    ADD CONSTRAINT admin_user
        FOREIGN KEY (id_admin_user)
            REFERENCES users (id_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: business_address (table: business)
ALTER TABLE business
    ADD CONSTRAINT business_address
        FOREIGN KEY (id_business_address)
            REFERENCES address (id_address)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


-- Reference: business_user (table: business)
ALTER TABLE business
    ADD CONSTRAINT business_user
        FOREIGN KEY (id_business_user)
            REFERENCES users (id_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_address (table: catering)
ALTER TABLE catering
    ADD CONSTRAINT catering_address
        FOREIGN KEY (id_catering_address)
            REFERENCES address (id_address)
            ON DELETE CASCADE
            ON UPDATE CASCADE
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_business (table: catering)
ALTER TABLE catering
    ADD CONSTRAINT catering_business
        FOREIGN KEY (id_business)
            REFERENCES business (id_business_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_for_location_catering (table: catering_location)
ALTER TABLE catering_location
    ADD CONSTRAINT catering_for_location_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_image_catering (table: catering_image)
ALTER TABLE catering_image
    ADD CONSTRAINT catering_image_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_item_catering (table: catering_item)
ALTER TABLE catering_item
    ADD CONSTRAINT catering_item_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_location_location (table: catering_location)
ALTER TABLE catering_location
    ADD CONSTRAINT catering_location_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_review_catering (table: catering_review)
ALTER TABLE catering_review
    ADD CONSTRAINT catering_review_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_review_customer (table: catering_review)
ALTER TABLE catering_review
    ADD CONSTRAINT catering_review_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: customer_address (table: customer)
ALTER TABLE customer
    ADD CONSTRAINT customer_address
        FOREIGN KEY (id_customer_address)
            REFERENCES address (id_address)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


-- Reference: customer_user (table: customer)
ALTER TABLE customer
    ADD CONSTRAINT customer_user
        FOREIGN KEY (id_customer_user)
            REFERENCES users (id_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: guest_customer (table: guest)
ALTER TABLE guest
    ADD CONSTRAINT guest_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_address (table: location)
ALTER TABLE location
    ADD CONSTRAINT location_address
        FOREIGN KEY (id_location_address)
            REFERENCES address (id_address)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_business (table: location)
ALTER TABLE location
    ADD CONSTRAINT location_business
        FOREIGN KEY (id_business)
            REFERENCES business (id_business_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_description_description_item (table: location_description)
ALTER TABLE location_description
    ADD CONSTRAINT location_description_description_item
        FOREIGN KEY (description_item_name)
            REFERENCES description_item (name)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_description_location (table: location_description)
ALTER TABLE location_description
    ADD CONSTRAINT location_description_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_for_event_location (table: location_for_event)
ALTER TABLE location_for_event
    ADD CONSTRAINT location_for_event_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_for_event_organized_event (table: location_for_event)
ALTER TABLE location_for_event
    ADD CONSTRAINT location_for_event_organized_event
        FOREIGN KEY (id_organized_event)
            REFERENCES organized_event (id_organized_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_image_location (table: location_image)
ALTER TABLE location_image
    ADD CONSTRAINT location_image_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_review_customer (table: location_review)
ALTER TABLE location_review
    ADD CONSTRAINT location_review_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_review_location (table: location_review)
ALTER TABLE location_review
    ADD CONSTRAINT location_review_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: optional_service_business (table: optional_service)
ALTER TABLE optional_service
    ADD CONSTRAINT optional_service_business
        FOREIGN KEY (id_business)
            REFERENCES business (id_business_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: optional_service_image_optional_service (table: optional_service_image)
ALTER TABLE optional_service_image
    ADD CONSTRAINT optional_service_image_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: organized_event_customer (table: organized_event)
ALTER TABLE organized_event
    ADD CONSTRAINT organized_event_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: organized_event_event_type (table: organized_event)
ALTER TABLE organized_event
    ADD CONSTRAINT organized_event_event_type
        FOREIGN KEY (id_event_type)
            REFERENCES event_type (id_event_type)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: organized_event_guest_guest (table: organized_event_guest)
ALTER TABLE organized_event_guest
    ADD CONSTRAINT organized_event_guest_guest
        FOREIGN KEY (id_guest)
            REFERENCES guest (id_guest)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: organized_event_guest_organized_event (table: organized_event_guest)
ALTER TABLE organized_event_guest
    ADD CONSTRAINT organized_event_guest_organized_event
        FOREIGN KEY (id_organized_event)
            REFERENCES organized_event (id_organized_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_for_event_location_for_event (table: service_for_event)
ALTER TABLE service_for_event
    ADD CONSTRAINT service_for_event_location_for_event
        FOREIGN KEY (id_location_for_event)
            REFERENCES location_for_event (id_location_for_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_for_event_optional_service (table: service_for_event)
ALTER TABLE service_for_event
    ADD CONSTRAINT service_for_event_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_review_customer (table: service_review)
ALTER TABLE service_review
    ADD CONSTRAINT service_review_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_review_optional_service (table: service_review)
ALTER TABLE service_review
    ADD CONSTRAINT service_review_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- End of file.


insert into address(country, city, street_name, street_number, zip_code)
VALUES ('Poland', 'Warsaw', 'Street 1', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 2', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 3', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 4', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 5', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 6', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 7', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 8', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 9', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 10', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 11', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 12', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 13', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 14', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 15', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 16', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 17', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 18', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 19', 1, '123456'),
       ('Poland', 'Warsaw', 'Street 20', 1, '123456');



INSERT into users (email, hashed_password, user_type)
VALUES ('admin@gmail.com', '$2y$12$CYhR7h46nkRx/tfFJn094eOKmWdMd1KEC.cyKcmLJNG76GxOTa/3G', 'A'),
       ('business@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('business3@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('business4@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('business5@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('business6@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('business7@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('business8@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B'),
       ('customer1@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C'),
       ('customer2@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C'),
       ('customer3@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C'),
       ('customer4@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C'),
       ('customer5@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C');



INSERT INTO admin (id_admin_user, access)
VALUES (1, 'FULL');


insert into business (id_business_user, first_name, last_name, business_name, phone_number,
                      verification_status, id_business_address)
values (2, 'firstName', 'lastName', 'businessName1', 123456789, 'NOT_VERIFIED', 1),
       (3, 'firstName3', 'lastName3', 'businessName3', 123456789, 'VERIFIED', 2),
       (4, 'firstName4', 'lastName4', 'businessName4', 123456789, 'VERIFIED', 3),
       (5, 'firstName5', 'lastName5', 'businessName5', 123456789, 'NOT_VERIFIED', 4),
       (6, 'firstName6', 'lastName6', 'businessName6', 123456789, 'VERIFICATION_PENDING', 5),
       (7, 'firstName7', 'lastName7', 'businessName7', 123456789, 'VERIFIED', 6),
       (8, 'firstName9', 'lastName8', 'businessName8', 12345678, 'NOT_VERIFIED', 7);



insert into catering(name, email, phone_number, service_cost, description, id_business, id_catering_address)
VALUES ('Happy Olympia', 'happyolympia@gmail.com', 12345678910, '50.00',
        'Chill Family Owned Restaurant for Close Gatherings with Greek Food', 2, 3),
       ('Bistro Turquoise', 'bistro_turquoise@gmail.com', 987656453421, '110.00',
        'A classy restaurant and lounge with hookah and serving mediterranean cuisine', 3, 4),
       ('Zapiecek', 'bistro_turquoise@gmail.com', 5464352373, '40.00',
        'Modern polish national cuisine', 4, 5),
       ('Uki Uki', 'uki_uki@gmail.com', 5464352373, '80.00',
        'Asian fusion cuisine', 5, 8),
       ('Kurkuma', 'kurkuma@gmail.com', 5464352373, '30.00',
        'Sophisticated and subtlev with use of the many spices, vegetables, grains and fruits grown across India. ', 6,
        4);

insert into catering_image (image, alt, id_catering)
VALUES ('https://www.definitelygreece.com/wp-content/uploads/2020/05/traditional-greek-food-meals.jpg', 'food_pic_1',
        1),
       ('https://media-cdn.tripadvisor.com/media/photo-s/19/ed/29/f0/fall-food-spread.jpg', 'food_pic_2',
        1),
       ('https://media-cdn.tripadvisor.com/media/photo-s/16/72/7f/07/mediterranean-bento-box.jpg', 'food_pic_1',
        2),
       ('https://res.cloudinary.com/jerrick/image/upload/fl_progressive,q_auto,w_1024/h1ztpdytqbip9kf3t7ua.jpg',
        'food_pic_2',
        2),
       ('https://d2joqs9jfh6k92.cloudfront.net/wp-content/uploads/2020/09/09113140/platzki-table-tp.jpg', 'food_pic_1',
        3),
       ('https://polishcuisine.net/wp-content/uploads/2020/09/2020.8.13KiebaskaPolishCuisineAuburnUberEatsJPEGS-6-1080x675.jpeg',
        'food_pic_2',
        3),
       ('https://www.rinag.com/wp-content/uploads/2020/02/Asian-Street-Kitchen-Festival-2019-min-1024x501.jpg',
        'food_pic_1',
        4),
       ('https://media.timeout.com/images/105441129/image.jpg', 'food_pic_2',
        4),
       ('http://images.ctfassets.net/3s5io6mnxfqz/6ZImCEzx6UuvuKaAiZEDDN/50479ee4a0902deb4eb1bab720ce248a/image1.jpg',
        'food_pic_1',
        5),
       ('https://www.wallpaperup.com/uploads/wallpapers/2014/11/09/511920/45482ae7629d9bbc982b907b8d90e776-700.jpg',
        'food_pic_2',
        5);


insert into catering_item (name, type, description, serving_price, is_vegan, is_vegetarian, is_gluten_free, id_catering)
VALUES ('App 1', 'Appetizer', 'App 1 description', '10.00', false, true, true, 1),
       ('App 2', 'Appetizer', 'App 2 description', '12.00', true, true, true, 1),
       ('App 3', 'Appetizer', 'App 3 description', '8.00', false, false, false, 2),
       ('App 4', 'Appetizer', 'App 4 description', '11.00', false, false, true, 2),
       ('App 5', 'Appetizer', 'App 5 description', '9.00', false, true, false, 3),
       ('App 6', 'Appetizer', 'App 6 description', '12.00', false, false, true, 4),
       ('App 7', 'Appetizer', 'App 7 description', '13.00', true, true, true, 5),
       ('Entree 1', 'Entree', 'Entree 1 description', '26.00', true, true, false, 1),
       ('Entree 2', 'Entree', 'Entree 2 description', '32.00', false, true, true, 2),
       ('Entree 3', 'Entree', 'Entree 3 description', '27.00', false, false, true, 3),
       ('Entree 4', 'Entree', 'Entree 4 description', '30.00', false, false, true, 3),
       ('Entree 5', 'Entree', 'Entree 5 description', '24.00', true, true, true, 4),
       ('Entree 6', 'Entree', 'Entree 6 description', '29.00', false, true, true, 4),
       ('Entree 7', 'Entree', 'Entree 7 description', '30.00', true, true, true, 5),
       ('Salad 1', 'Salad', 'Salad 1 description', '10.00', true, true, true, 2),
       ('Salad 2', 'Salad', 'Salad 2 description', '15.00', false, true, true, 3),
       ('Salad 3', 'Salad', 'Salad 3 description', '13.00', false, true, true, 3),
       ('Salad 4', 'Salad', 'Salad 4 description', '18.00', false, true, false, 4),
       ('Salad 5', 'Salad', 'Salad 5 description', '18.00', false, false, false, 5),
       ('Salad 6', 'Salad', 'Salad 6 description', '14.00', false, true, false, 5),
       ('Soup 1', 'Soup', 'Soup 1 description', '10.00', true, true, true, 2),
       ('Soup 2', 'Soup', 'Soup 2 description', '12.00', false, true, false, 3),
       ('Soup 3', 'Soup', 'Soup 3 description', '15.00', false, false, false, 3),
       ('Soup 4', 'Soup', 'Soup 4 description', '10.00', false, true, true, 4),
       ('Soup 5', 'Soup', 'Soup 5 description', '13.00', false, true, false, 5),
       ('Dessert 1', 'Dessert', 'Dessert 1 description', '13.00', true, true, true, 2),
       ('Dessert 2', 'Dessert', 'Dessert 2 description', '11.00', false, true, false, 3),
       ('Dessert 3', 'Dessert', 'Dessert 3 description', '14.00', true, true, true, 4),
       ('Dessert 4', 'Dessert', 'Dessert 4 description', '15.00', true, true, true, 3),
       ('Dessert 5', 'Dessert', 'Dessert 5 description', '10.00', false, true, true, 5),
       ('Dessert 6', 'Dessert', 'Dessert 6 description', '8.00', false, false, true, 4),
       ('Dessert 7', 'Dessert', 'Dessert 7 description', '12.00', true, true, true, 2);


insert into location (name, email, phone_number, seating_capacity, standing_capacity, description, daily_rent_cost,
                      size_in_sq_meters, id_business, id_location_address)
VALUES ('Happy Olympia', 'happyolympia@gmail.com', 123456789, 40, 100,
        'Chill Family Owned Restaurant for Close Gatherings with Greek Food', 0.00, 200, 2, 1),
       ('Modern Art Gallery', 'gallery@gmail.com', 123456789, 500, 500, 'Fancy art gallery in the heart of the city ',
        '1000.00', 300, 3, 15),
       ('Downtown Community Center', 'comm_center@gmail.com', 123456789, 100, 150,
        'Community Center with a hall open for gatherings', '200.00', 400, 8, 3),
       ('One Tower', 'tower_business@gmail.com', 123456789, 200, 300,
        'Business Center located in the One Tower on the 35th floor', '200.00', 200, 7, 18),
       ('Party Time', 'party247@gmail.com', 123456789, 120, 250, 'A dance hall that stays open late at night',
        '500.00', 400, 6, 17),
       ('Bistro Turquoise', 'bistro_turquoise@gmail.com', 987654321, 32, 50,
        'A classy restaurant and lounge with hookah and serving mediterranean cuisine', '0.00', 250, 5, 2);


insert into catering_location (id_location, id_catering)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 1),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4),
       (4, 5),
       (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5),
       (6, 1),
       (6, 2),
       (6, 3),
       (6, 4),
       (6, 5);

insert into description_item(name, description)
VALUES ('Outside Catering Available', 'Catering from other outside locations is available'),
       ('Can Bring Own Food', 'It is possible to bring and consume your own food to the location'),
       ('Can Bring Own Alcohol', 'It is possible to bring and consume your own alcohol to the location'),
       ('Has Projector', 'A projector is available at the location'),
       ('Has Stage', 'A stage is available at the location'),
       ('Has Wi-Fi', 'It is possible to connect to a wireless network at the location'),
       ('Wheelchair Accessible', 'No staircase. A ramp or elevator available at the location'),
       ('Has Patio', 'There is an outside patio accessible at the location'),
       ('Serves Food', 'it is possible to order meals at the location');


insert into location_description (id_location, description_item_name)
VALUES (1, 'Outside Catering Available'),
       (1, 'Has Wi-Fi'),
       (1, 'Wheelchair Accessible'),
       (1, 'Serves Food'),
       (2, 'Can Bring Own Alcohol'),
       (2, 'Has Projector'),
       (2, 'Has Wi-Fi'),
       (2, 'Outside Catering Available'),
       (3, 'Can Bring Own Food'),
       (3, 'Has Stage'),
       (3, 'Has Wi-Fi'),
       (3, 'Wheelchair Accessible'),
       (3, 'Outside Catering Available'),
       (4, 'Has Projector'),
       (4, 'Has Stage'),
       (4, 'Has Wi-Fi'),
       (4, 'Wheelchair Accessible'),
       (4, 'Outside Catering Available'),
       (5, 'Has Projector'),
       (5, 'Has Wi-Fi'),
       (5, 'Has Stage'),
       (5, 'Has Patio'),
       (5, 'Serves Food'),
       (6, 'Has Wi-Fi'),
       (6, 'Wheelchair Accessible'),
       (6, 'Has Patio'),
       (6, 'Serves Food');

insert into location_image (image, alt, id_location)
VALUES ('https://infatuation.s3.amazonaws.com/media/images/guides/greek-restaurants-nyc/kasinger_nyc_zenontaverna_exterior002.jpg',
        'outside_pic_1', 1),
       ('https://infatuation.s3.amazonaws.com/media/images/guides/greek-restaurants-nyc/kasinger_nyc_tellys004.jpg',
        'interior_pic_2', 1),
       ('https://iadsb.tmgrup.com.tr/61c5f5/1200/627/0/512/800/930?u=https://idsb.tmgrup.com.tr/2019/12/22/1577016108432.jpg',
        'interior_pic_1', 2),
       ('https://d32dm0rphc51dk.cloudfront.net/cdojTownvkIDQ3_N4877nw/wide.jpg', 'interior_pic_2', 2),
       ('https://sleep-fly.com/wp-content/uploads/2019/07/Fotograf%C3%ADa-010.jpg', 'interior_pic_1', 3),
       ('https://image.freepik.com/free-photo/modern-meeting-room-with-table-chairs-concept-conventon-room-conference-room_82984-136.jpg',
        'interior_pic_2', 3),
       ('https://harriman.com/wp-content/uploads/presque-isle-preferred.jpg', 'outside_pic_1', 4),
       ('https://www.primeeng.com/wp-content/uploads/2019/02/Glenwood-Recreation-2.jpg', 'interior_pic_2', 4),
       ('https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_280,q_50,w_640/v1/clients/denver/OPEHLIAS_9491_2a725b87-78af-4bfd-9d09-d81338089cf7.jpg',
        'interior_pic_1', 5),
       ('https://i.pinimg.com/originals/f8/5e/aa/f85eaa36ed5536ce28b9c56e17d66ced.jpg', 'interior_pic_1', 5),
       ('http://retaildesignblog.net/wp-content/uploads/2016/03/Hookah-Bar-Nargile-by-KMAN-Studio-Sofia-Bulgaria-03.jpg',
        'interior_pic_1', 6),
       ('http://www.itsliquid.com/wp-content/uploads/2016/04/002-13.jpg', 'interior_pic_2', 6),
       ('https://i.pinimg.com/originals/42/06/92/420692b178ff8b05aea4a48176744a30.jpg', 'outside_pic_1', 6);



INSERT INTO customer (id_customer_user, first_name, last_name, birthdate, phone_number,
                      id_customer_address)
VALUES (9, 'Lacy', 'Botsford', '1988-06-30', 123456789, 20),
       (13, 'Tommie', 'Bechtelar', '1990-09-24', 123456789, 19),
       (11, 'Delia', 'Koch', '1985-11-16', 123456789, 18),
       (12, 'Tracey', 'Metz', '2000-03-16', 123456789, 17),
       (10, 'Oma', 'Walter', '1975-11-08', 123456789, 16);


INSERT INTO guest (first_name, last_name, email, id_customer)
VALUES ('Else', 'Ryan', 'Ryan12@gmail.com', 9),
       ('AWE', 'Ryan', 'Ryan12@gmail.com', 10),
       ('Giovanny', 'Jast', 'Jast71@hotmail.com', 11),
       ('Titus', 'Lemke', 'Lemke81@yahoo.com', 12),
       ('Breanna', 'Goldner', 'Goldner00@hotmail.com', 13),
       ('Joy', 'Schimmel', 'Schimmel14@gmail.com', 9);


insert into optional_service(alias, type, email, description, service_cost, id_business)
values ('alias1', 'Singer', 'email@gmail.com', 'description1', '100.00', 8),
       ('alias2', 'DJ', 'email@gmail.com', 'description2', '200.00', 6),
       ('alias3', 'Music Band', 'email@gmail.com', 'description3', '300.00', 3),
       ('alias4', 'Translator', 'email@gmail.com', 'description4', '400.00', 4),
       ('alia5', 'Singer', 'email@gmail.com', 'description5', '250.00', 5),
       ('alias6', 'Kids Performer', 'email@gmail.com', 'description6', '160.00', 6),
       ('alias7', 'Musician', 'email@gmail.com', 'description7', '70.00', 7),
       ('alias8', 'Photographer', 'email@gmail.com', 'description8', '180.00', 4),
       ('alias9', 'Host', 'email@gmail.com', 'description9', '290.00', 4),
       ('alias10', 'Photographer', 'email@gmail.com', 'description10', '140.00', 4);


insert into optional_service_image(image, alt, id_optional_service)
values ('img1', 'alt1', 1),
       ('img2', 'alt2', 2),
       ('img3', 'alt3', 3),
       ('img4', 'alt4', 4),
       ('img5', 'alt5', 5),
       ('img6', 'alt6', 6),
       ('img7', 'alt7', 7),
       ('img8', 'alt8', 8),
       ('img9', 'alt9', 9),
       ('img10', 'alt10', 10);


insert into service_review(title, comment, id_optional_service, id_customer)
values ('title1', 'comment1', 1, 9),
       ('title2', 'comment2', 2, 10),
       ('title3', 'comment3', 3, 11),
       ('title4', 'comment4', 4, 12),
       ('title5', 'comment5', 5, 13),
       ('title6', 'comment6', 6, 9),
       ('title7', 'comment7', 7, 10),
       ('title8', 'comment8', 8, 11),
       ('title9', 'comment9', 9, 12),
       ('title10', 'comment10', 10, 13);


insert into catering_review(title, comment, id_customer, id_catering, star_review)
values ('title1', 'comment1', 9, 1, 1),
       ('title2', 'comment2', 10, 2, 2),
       ('title3', 'comment3', 11, 3, 3),
       ('title4', 'comment4', 12, 4, 4),
       ('title5', 'comment5', 13, 5, 5),
       ('title6', 'comment6', 9, 1, 4),
       ('title7', 'comment7', 10, 2, 5),
       ('title8', 'comment8', 11, 3, 5),
       ('title9', 'comment9', 12, 4, 4),
       ('title10', 'comment10', 13, 5, 2);


INSERT INTO location_review (title, comment, id_location, id_customer)
VALUES ('False advertisement', 'Location was alright but it simply was not as big as advertised.', 1, 9),
       ('Great place!', 'I cannot recommend it enough. I love it, will come again.', 2, 10),
       ('Horrible shack.', 'Location was alright but not fit to my tastes.', 3, 11),
       ('It was so-so', 'It was ok, but not great.', 4, 12),
       ('Just perfect.', 'Clean, spacious and with great customer service. You will not be dissapointed!', 5, 13);


INSERT INTO event_type (type, description)
VALUES ('Wedding', 'Description1'),
       ('Conference', 'Description2'),
       ('Seminar', 'Description3'),
       ('Birthday', 'Description4'),
       ('Party', 'Description5');

INSERT INTO organized_event (start_date, is_predefined, event_status, id_customer, id_event_type)
VALUES ('2021-03-23', false, 'READY', 9, 1),
       ('2021-03-29', false, 'IN_PLANNING', 10, 3),
       ('2021-04-30', false, 'IN_PLANNING', 11, 5),
       ('2021-05-05', false, 'FINISHED', 12, 2),
       ('2021-05-01', false, 'FINISHED', 13, 5);


INSERT INTO organized_event_guest (id_guest, id_organized_event)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);


insert into location_for_event(time_from, time_to, id_organized_event, id_location)
values ('2021-09-01 00:00:00', '2021-09-01 23:59:59', 1, 1),
       ('2021-09-02 00:00:00', '2021-09-02 23:59:59', 2, 2),
       ('2021-09-03 00:00:00', '2021-09-03 23:59:59', 3, 3),
       ('2021-09-04 00:00:00', '2021-09-04 23:59:59', 4, 3),
       ('2021-09-05 00:00:00', '2021-09-05 23:59:59', 5, 4),
       ('2021-09-06 00:00:00', '2021-09-06 23:59:59', 1, 5),
       ('2021-09-07 00:00:00', '2021-09-07 23:59:59', 2, 2),
       ('2021-09-08 00:00:00', '2021-09-08 23:59:59', 3, 1),
       ('2021-09-09 00:00:00', '2021-09-09 23:59:59', 4, 3),
       ('2021-09-10 00:00:00', '2021-09-10 23:59:59', 5, 4);


insert into service_for_event(time_from, time_to, comment, id_optional_service, id_location_for_event)
values ('2021-09-01 12:00:00', '2021-09-01 12:00:00', 'comment1', 1, 1),
       ('2021-09-02 12:00:00', '2021-09-02 12:00:00', 'comment2', 2, 2),
       ('2021-09-03 12:00:00', '2021-09-03 12:00:00', 'comment3', 3, 3),
       ('2021-09-04 12:00:00', '2021-09-04 12:00:00', 'comment4', 4, 4),
       ('2021-09-05 12:00:00', '2021-09-05 12:00:00', 'comment5', 5, 5),
       ('2021-09-06 12:00:00', '2021-09-06 12:00:00', 'comment6', 6, 7),
       ('2021-09-07 12:00:00', '2021-09-07 12:00:00', 'comment7', 7, 8),
       ('2021-09-08 12:00:00', '2021-09-08 12:00:00', 'comment8', 8, 9),
       ('2021-09-09 12:00:00', '2021-09-09 12:00:00', 'comment9', 9, 1),
       ('2021-09-10 12:00:00', '2021-09-10 12:00:00', 'comment10', 10, 10);

insert into app_problem(date_time, concern, description, id_user)
VALUES ('2021-02-13', 'Bugged submit button',
        'Submit button does nothing when trying to submit changes.', 9),
       ('2021-1-22', 'Cannot post review', 'Review is missing when posted.', 10),
       ('2020-12-18', 'Cannot see events history', 'Unable to open history of events', 9),
       ('2020-11-11', 'Problems with registration', 'Cannot register.', 12),
       ('2021-03-18', 'Problems with logging in', 'Could not log in.', 11);

ALTER TABLE location_description
    RENAME COLUMN description_item_name TO name;

alter table organized_event
    add name varchar(100);


update organized_event
set name='Julie and Jon"s wedding'
where id_organized_event = 1;

-- Table: catering_for_chosen_location
CREATE TABLE catering_for_chosen_location
(
    id_catering_for_chosen_location serial    NOT NULL,
    date_time                       timestamp NOT NULL,
    id_catering                     int       NOT NULL,
    CONSTRAINT catering_for_chosen_location_pk PRIMARY KEY (id_catering_for_chosen_location)
);

-- Table: catering_order_choice
CREATE TABLE catering_order_choice
(
    id_catering_item                int NOT NULL,
    count                           int NOT NULL,
    id_catering_for_chosen_location int NOT NULL,
    CONSTRAINT catering_order_choice_pk PRIMARY KEY (id_catering_item)
);


-- Reference: catering_order_catering (table: catering_for_chosen_location)
ALTER TABLE catering_for_chosen_location
    ADD CONSTRAINT catering_order_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_order_choice_catering_for_chosen_location (table: catering_order_choice)
ALTER TABLE catering_order_choice
    ADD CONSTRAINT catering_order_choice_catering_for_chosen_location
        FOREIGN KEY (id_catering_for_chosen_location)
            REFERENCES catering_for_chosen_location (id_catering_for_chosen_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_order_choice_catering_item (table: catering_order_choice)
ALTER TABLE catering_order_choice
    ADD CONSTRAINT catering_order_choice_catering_item
        FOREIGN KEY (id_catering_item)
            REFERENCES catering_item (id_catering_item)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE;

alter table location_for_event
    add id_catering_for_chosen_location int;

-- Reference: location_for_event_catering_order (table: location_for_event)
ALTER TABLE location_for_event
    ADD CONSTRAINT location_for_event_catering_order
        FOREIGN KEY (id_catering_for_chosen_location)
            REFERENCES catering_for_chosen_location (id_catering_for_chosen_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

insert into catering_for_chosen_location (date_time, id_catering)
VALUES ('2021-07-15 15:00', 5);

insert into catering_order_choice (id_catering_item, count, id_catering_for_chosen_location)
VALUES (7, 12, 1),
       (4, 12, 1),
       (20, 6, 1),
       (25, 6, 1),
       (30, 10, 1);