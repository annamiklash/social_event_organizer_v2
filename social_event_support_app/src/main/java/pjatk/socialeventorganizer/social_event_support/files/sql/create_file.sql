
INSERT INTO organized_event (date, event_status, id_customer, id_event_type, created_at,
                             modified_at, deleted_at, name)
VALUES ('2021-03-23', 'READY', 9, 1, (select current_timestamp), (select current_timestamp), null,
        'CELEBRATION'),
       ('2021-03-29', 'IN_PLANNING', 10, 3, (select current_timestamp), (select current_timestamp), null,
        'CELEBRATION'),
       ('2021-04-30', 'IN_PLANNING', 11, 5, (select current_timestamp), (select current_timestamp), null,
        'CELEBRATION'),
       ('2021-05-05', 'FINISHED', 12, 2, (select current_timestamp), (select current_timestamp), null,
        'CELEBRATION'),
       ('2021-05-01', 'FINISHED', 13, 5, (select current_timestamp), (select current_timestamp), null,
        'CELEBRATION');


insert into location_for_event(time_from, time_to, id_organized_event, id_location, guests, confirmation_status)
values ('2021-09-01 00:00:00', '2021-09-01 23:59:59', 1, 1, 10, 'CONFIRMED'),
       ('2021-09-02 00:00:00', '2021-09-02 23:59:59', 2, 2, 10, 'CONFIRMED'),
       ('2021-09-03 00:00:00', '2021-09-03 23:59:59', 3, 3, 10, 'CONFIRMED'),
       ('2021-09-04 00:00:00', '2021-09-04 23:59:59', 4, 3, 10, 'CONFIRMED'),
       ('2021-09-05 00:00:00', '2021-09-05 23:59:59', 5, 4, 10, 'CONFIRMED'),
       ('2021-09-06 00:00:00', '2021-09-06 23:59:59', 1, 5, 10, 'CONFIRMED'),
       ('2021-09-07 00:00:00', '2021-09-07 23:59:59', 2, 2, 10, 'CONFIRMED'),
       ('2021-09-08 00:00:00', '2021-09-08 23:59:59', 3, 1, 10, 'CONFIRMED'),
       ('2021-09-09 00:00:00', '2021-09-09 23:59:59', 4, 3, 10, 'CONFIRMED'),
       ('2021-09-10 00:00:00', '2021-09-10 23:59:59', 5, 4, 10, 'CONFIRMED');


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


insert into catering_for_chosen_location (date_time, id_catering, id_location_for_event)
VALUES ('2021-09-01 12:00:00', 4, 4);

insert into catering_order_choice (id_catering_item, count, id_catering_for_chosen_location)
VALUES (5, 3, 1),
       (6, 3, 1);


create table location_availability
(
    id_availability serial      NOT NULL,
    date            timestamp   NOT NULL,
    time_from       timestamp   NOT NULL,
    time_to         timestamp   NOT NULL,
    status          varchar(30) not null,
    id_location     int         NOT NULL,
    CONSTRAINT location_availability_pk PRIMARY KEY (id_availability)
);

-- Reference: location_location_availability (table: location_availability)
ALTER TABLE location_availability
    ADD CONSTRAINT location_location_availability
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

create table catering_availability
(
    id_availability serial      NOT NULL,
    status          varchar(30) NOT NULL,
    date            timestamp   NOT NULL,
    time_from       timestamp   NOT NULL,
    time_to         timestamp   NOT NULL,
    id_catering     int         NOT NULL,
    CONSTRAINT catering_availability_pk PRIMARY KEY (id_availability)
);

-- Reference: catering_catering_availability (table: catering_availability)
ALTER TABLE catering_availability
    ADD CONSTRAINT catering_catering_availability
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


create table optional_service_availability
(
    id_availability     serial      NOT NULL,
    status              varchar(30) NOT NULL,
    date                timestamp   NOT NULL,
    time_from           timestamp   NOT NULL,
    time_to             timestamp   NOT NULL,
    id_optional_service int         NOT NULL,
    CONSTRAINT optional_service_availability_pk PRIMARY KEY (id_availability)
);

-- Reference: catering_catering_availability (table: catering_availability)
ALTER TABLE optional_service_availability
    ADD CONSTRAINT optional_service_optional_service_availability
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Table: catering_cuisine
CREATE TABLE catering_cuisine
(
    id_catering int NOT NULL,
    id_cuisine  int NOT NULL,
    CONSTRAINT catering_cuisine_pk PRIMARY KEY (id_catering, id_cuisine)
);

-- Table: cuisine
CREATE TABLE cuisine
(
    id_cuisine serial      NOT NULL,
    name       varchar(50) NOT NULL,
    CONSTRAINT cuisine_pk PRIMARY KEY (id_cuisine)
);

-- foreign keys
-- Reference: catering_cuisine_catering (table: catering_cuisine)
ALTER TABLE catering_cuisine
    ADD CONSTRAINT catering_cuisine_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

insert into cuisine (name)
VALUES ('Mexican'),
       ('Italian'),
       ('Greek'),
       ('French'),
       ('Thai'),
       ('Spanish'),
       ('Indian'),
       ('Mediterranean'),
       ('Polish'),
       ('Russian'),
       ('Vietnamese'),
       ('Japanese'),
       ('Chinese'),
       ('Vegan'),
       ('Fusion'),
       ('Vegetarian'),
       ('Turkish');

insert into catering_cuisine (id_catering, id_cuisine)
VALUES (1, 3),
       (1, 16),
       (1, 6),
       (2, 4),
       (2, 1),
       (2, 2),
       (3, 17),
       (3, 14),
       (3, 10),
       (4, 9),
       (4, 11),
       (5, 15),
       (5, 1),
       (5, 7);


CREATE TABLE customer_avatar
(
    id_avatar serial NOT NULL,
    image     bytea  NOT NULL,
    CONSTRAINT customer_avatar_pk PRIMARY KEY (id_avatar)
);

alter table customer
    add id_avatar int default null;

-- Reference: user_user_avatar (table: user)
ALTER TABLE customer
    ADD CONSTRAINT user_user_avatar
        FOREIGN KEY (id_avatar)
            REFERENCES customer_avatar (id_avatar)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;