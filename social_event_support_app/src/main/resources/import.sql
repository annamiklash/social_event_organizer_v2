insert into address(country, city, street_name, street_number, zip_code, created_at, modified_at, deleted_at)
VALUES ('Poland', 'Warsaw', 'Street 1', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 2', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 3', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 4', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 5', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 6', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 7', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 8', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 9', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 10', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 11', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 12', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 13', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 14', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 15', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 16', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 17', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 18', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 19', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 20', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Street 20', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null),
       ('Poland', 'Warsaw', 'Streetttttt', 1, '123456', (select current_timestamp), (select current_timestamp), null);



INSERT into users (email, hashed_password, user_type, created_at, modified_at, deleted_at, blocked_at, is_active)
VALUES ('admin@gmail.com', '$2y$12$CYhR7h46nkRx/tfFJn094eOKmWdMd1KEC.cyKcmLJNG76GxOTa/3G', 'A',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business3@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business4@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business5@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business6@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business7@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('business8@email.com', '$2y$13$FB2Bur0wo.kd6G7.d8Tfne.iVpBE1OAi4FPrCUHrbZJj/2uVikofK', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('customer1@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('customer2@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('customer3@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('customer4@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('customer5@email.com', '$2y$12$xCfIcKwTBTLrhv6XLIDMKuvnYJkWJajqdodBEh5SJqFaHdS6RZUyC', 'C',
        (select current_timestamp), (select current_timestamp), null, null, true),
       ('new_business@gmail.com', '$2a$10$gkZPL4pIC.nMV6ynCjkhjeW4/Jqd7hCmtb06ISOvXhCDNshqjYF9i', 'B',
        (select current_timestamp), (select current_timestamp), null, null, true);



INSERT INTO admins (id_admin_user, access)
VALUES (1, 'FULL');


insert into business (id_business_user, first_name, last_name, business_name, phone_number,
                      verification_status, id_business_address)
values (2, 'firstName', 'lastName', 'businessName1', 123456789, 'NOT_VERIFIED', 1),
       (3, 'firstName3', 'lastName3', 'businessName3', 123456789, 'VERIFIED', 2),
       (4, 'firstName4', 'lastName4', 'businessName4', 123456789, 'VERIFIED', 3),
       (5, 'firstName5', 'lastName5', 'businessName5', 123456789, 'NOT_VERIFIED', 4),
       (6, 'firstName6', 'lastName6', 'businessName6', 123456789, 'VERIFICATION_PENDING', 5),
       (7, 'firstName7', 'lastName7', 'businessName7', 123456789, 'VERIFIED', 6),
       (8, 'firstName9', 'lastName8', 'businessName8', 12345678, 'NOT_VERIFIED', 7),
       (14, 'Test', 'Test', 'businessName8', 12345678, 'VERIFIED', 21);



insert into catering(name, email, phone_number, service_cost, description, id_business, id_catering_address, created_at,
                     modified_at, deleted_at)
VALUES ('Happy Olympia', 'happyolympia@gmail.com', 12345678910, '50.00',
        'Chill Family Owned Restaurant for Close Gatherings with Greek Food', 2, 3, (select current_timestamp),
        (select current_timestamp), null),
       ('Bistro Turquoise', 'bistro_turquoise@gmail.com', 987656453421, '110.00',
        'A classy restaurant and lounge with hookah and serving mediterranean cuisine', 3, 4,
        (select current_timestamp), (select current_timestamp), null),
       ('Zapiecek', 'bistro_turquoise@gmail.com', 5464352373, '40.00',
        'Modern polish national cuisine', 4, 5, (select current_timestamp), (select current_timestamp), null),
       ('Uki Uki', 'uki_uki@gmail.com', 5464352373, '80.00',
        'Asian fusion cuisine', 5, 8, (select current_timestamp), (select current_timestamp), null),
       ('Kurkuma', 'kurkuma@gmail.com', 5464352373, '30.00',
        'Sophisticated and subtlev with use of the many spices, vegetables, grains and fruits grown across India. ', 6,
        4, (select current_timestamp), (select current_timestamp), null);

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


insert into catering_item (name, type, description, serving_price, is_vegan, is_vegetarian, is_gluten_free, id_catering,
                           created_at, modified_at)
VALUES ('App 1', 'Appetizer', 'App 1 description', '10.00', false, true, true, 1, (select current_timestamp),
        (select current_timestamp)),
       ('App 2', 'Appetizer', 'App 2 description', '12.00', true, true, true, 1, (select current_timestamp),
        (select current_timestamp)),
       ('App 3', 'Appetizer', 'App 3 description', '8.00', false, false, false, 2, (select current_timestamp),
        (select current_timestamp)),
       ('App 4', 'Appetizer', 'App 4 description', '11.00', false, false, true, 2, (select current_timestamp),
        (select current_timestamp)),
       ('App 5', 'Appetizer', 'App 5 description', '9.00', false, true, false, 3, (select current_timestamp),
        (select current_timestamp)),
       ('App 6', 'Appetizer', 'App 6 description', '12.00', false, false, true, 4, (select current_timestamp),
        (select current_timestamp)),
       ('App 7', 'Appetizer', 'App 7 description', '13.00', true, true, true, 5, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 1', 'Entree', 'Entree 1 description', '26.00', true, true, false, 1, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 2', 'Entree', 'Entree 2 description', '32.00', false, true, true, 2, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 3', 'Entree', 'Entree 3 description', '27.00', false, false, true, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 4', 'Entree', 'Entree 4 description', '30.00', false, false, true, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 5', 'Entree', 'Entree 5 description', '24.00', true, true, true, 4, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 6', 'Entree', 'Entree 6 description', '29.00', false, true, true, 4, (select current_timestamp),
        (select current_timestamp)),
       ('Entree 7', 'Entree', 'Entree 7 description', '30.00', true, true, true, 5, (select current_timestamp),
        (select current_timestamp)),
       ('Salad 1', 'Salad', 'Salad 1 description', '10.00', true, true, true, 2, (select current_timestamp),
        (select current_timestamp)),
       ('Salad 2', 'Salad', 'Salad 2 description', '15.00', false, true, true, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Salad 3', 'Salad', 'Salad 3 description', '13.00', false, true, true, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Salad 4', 'Salad', 'Salad 4 description', '18.00', false, true, false, 4, (select current_timestamp),
        (select current_timestamp)),
       ('Salad 5', 'Salad', 'Salad 5 description', '18.00', false, false, false, 5, (select current_timestamp),
        (select current_timestamp)),
       ('Salad 6', 'Salad', 'Salad 6 description', '14.00', false, true, false, 5, (select current_timestamp),
        (select current_timestamp)),
       ('Soup 1', 'Soup', 'Soup 1 description', '10.00', true, true, true, 2, (select current_timestamp),
        (select current_timestamp)),
       ('Soup 2', 'Soup', 'Soup 2 description', '12.00', false, true, false, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Soup 3', 'Soup', 'Soup 3 description', '15.00', false, false, false, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Soup 4', 'Soup', 'Soup 4 description', '10.00', false, true, true, 4, (select current_timestamp),
        (select current_timestamp)),
       ('Soup 5', 'Soup', 'Soup 5 description', '13.00', false, true, false, 5, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 1', 'Dessert', 'Dessert 1 description', '13.00', true, true, true, 2, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 2', 'Dessert', 'Dessert 2 description', '11.00', false, true, false, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 3', 'Dessert', 'Dessert 3 description', '14.00', true, true, true, 4, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 4', 'Dessert', 'Dessert 4 description', '15.00', true, true, true, 3, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 5', 'Dessert', 'Dessert 5 description', '10.00', false, true, true, 5, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 6', 'Dessert', 'Dessert 6 description', '8.00', false, false, true, 4, (select current_timestamp),
        (select current_timestamp)),
       ('Dessert 7', 'Dessert', 'Dessert 7 description', '12.00', true, true, true, 2, (select current_timestamp),
        (select current_timestamp));


insert into location (name, email, phone_number, seating_capacity, standing_capacity, description, daily_rent_cost,
                      size_in_sq_meters, id_business, id_location_address, created_at, modified_at, deleted_at)
VALUES ('Happy Olympia', 'happyolympia@gmail.com', 123456789, 40, 100,
        'Chill Family Owned Restaurant for Close Gatherings with Greek Food', 0.00, 200, 2, 1,
        (select current_timestamp), (select current_timestamp), null),
       ('Modern Art Gallery', 'gallery@gmail.com', 123456789, 500, 500, 'Fancy art gallery in the heart of the city ',
        '1000.00', 300, 3, 15,
        (select current_timestamp), (select current_timestamp), null),
       ('Downtown Community Center', 'comm_center@gmail.com', 123456789, 100, 150,
        'Community Center with a hall open for gatherings', '200.00', 400, 8, 3,
        (select current_timestamp), (select current_timestamp), null),
       ('One Tower', 'tower_business@gmail.com', 123456789, 200, 300,
        'Business Center located in the One Tower on the 35th floor', '200.00', 200, 7, 18,
        (select current_timestamp), (select current_timestamp), null),
       ('Party Time', 'party247@gmail.com', 123456789, 120, 250, 'A dance hall that stays open late at night',
        '500.00', 400, 6, 17,
        (select current_timestamp), (select current_timestamp), null),
       ('Bistro Turquoise', 'bistro_turquoise@gmail.com', 987654321, 32, 50,
        'A classy restaurant and lounge with hookah and serving mediterranean cuisine', '0.00', 250, 5, 2,
        (select current_timestamp), (select current_timestamp), null);


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
       ('Has WiFi', 'It is possible to connect to a wireless network at the location'),
       ('Wheelchair Accessible', 'No staircase. A ramp or elevator available at the location'),
       ('Has Patio', 'There is an outside patio accessible at the location'),
       ('Serves Food', 'it is possible to order meals at the location');


insert into location_description (id_location, name)
VALUES (1, 'Outside Catering Available'),
       (1, 'Has WiFi'),
       (1, 'Wheelchair Accessible'),
       (1, 'Serves Food'),
       (2, 'Can Bring Own Alcohol'),
       (2, 'Has Projector'),
       (2, 'Has WiFi'),
       (2, 'Outside Catering Available'),
       (3, 'Can Bring Own Food'),
       (3, 'Has Stage'),
       (3, 'Has WiFi'),
       (3, 'Wheelchair Accessible'),
       (3, 'Outside Catering Available'),
       (4, 'Has Projector'),
       (4, 'Has Stage'),
       (4, 'Has WiFi'),
       (4, 'Wheelchair Accessible'),
       (4, 'Outside Catering Available'),
       (5, 'Has Projector'),
       (5, 'Has WiFi'),
       (5, 'Has Stage'),
       (5, 'Has Patio'),
       (5, 'Serves Food'),
       (6, 'Has WiFi'),
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



INSERT INTO customer (id_customer_user, first_name, last_name, birthdate, phone_number)
VALUES (9, 'Lacy', 'Botsford', '1988-06-30', 123456789),
       (13, 'Tommie', 'Bechtelar', '1990-09-24', 123456789),
       (11, 'Delia', 'Koch', '1985-11-16', 123456789),
       (12, 'Tracey', 'Metz', '2000-03-16', 123456789),
       (10, 'Oma', 'Walter', '1975-11-08', 123456789);


INSERT INTO guest (first_name, last_name, email, id_customer, created_at, modified_at)
VALUES ('Else', 'Ryan', 'ryan12@gmail.com', 9, (select current_timestamp), (select current_timestamp)),
       ('Awe', 'Ryan', 'ryan12@gmail.com', 10, (select current_timestamp), (select current_timestamp)),
       ('Giovanny', 'Jast', 'jast71@hotmail.com', 11, (select current_timestamp), (select current_timestamp)),
       ('Titus', 'Lemke', 'lemke81@yahoo.com', 12, (select current_timestamp), (select current_timestamp)),
       ('Breanna', 'Goldner', 'Goldner00@hotmail.com', 13, (select current_timestamp), (select current_timestamp)),
       ('Joy', 'Schimmel', 'schimmel14@gmail.com', 9, (select current_timestamp), (select current_timestamp));


insert into optional_service(alias, type, email, description, service_cost, id_business, created_at, modified_at,
                             deleted_at, kids_performer_type, age_from, age_to, instrument, people_count, first_name,
                             last_name, d_type, id_service_address)
values ('alias1', 'SINGER', 'email@gmail.com', 'description1', '100.00', 8, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, null, 'Jon', 'Doe', 'SINGER',22),
       ('alias2', 'DJ', 'email@gmail.com', 'description2', '200.00', 6, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, null, 'Jon', 'Doe', 'DJ',23),
       ('alias3', 'MUSIC BAND', 'email@gmail.com', 'description3', '300.00', 3, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, 4, 'Jon', 'Doe', 'MUSIC BAND',24),
       ('alias4', 'INTERPRETER', 'email@gmail.com', 'description4', '400.00', 4, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, null, 'Jon', 'Doe', 'INTERPRETER',25),
       ('alia5', 'SINGER', 'email@gmail.com', 'description5', '250.00', 5, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, null, 'Jon', 'Doe', 'SINGER',26),
       ('alias6', 'KIDS PERFORMER', 'email@gmail.com', 'description6', '160.00', 6, (select current_timestamp),
        (select current_timestamp), null, 'CLOWN', 5, 12, null, null, 'Jon', 'Doe','KIDS PERFORMER', 27),
       ('alias7', 'MUSICIAN', 'email@gmail.com', 'description7', '70.00', 7, (select current_timestamp),
        (select current_timestamp), null, null, null, null, 'GUITAR', null, 'Jon', 'Doe', 'MUSICIAN', 28),
       ('alias8', 'MUSICIAN', 'email@gmail.com', 'description8', '180.00', 4, (select current_timestamp),
        (select current_timestamp), null, null, null, null, 'PIANO', null, 'Jon', 'Doe', 'MUSICIAN', 29),
       ('alias9', 'HOST', 'email@gmail.com', 'description9', '290.00', 4, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, null, 'Jon', 'Doe', 'HOST', 30),
       ('alias10', 'INTERPRETER', 'email@gmail.com', 'description10', '140.00', 4, (select current_timestamp),
        (select current_timestamp), null, null, null, null, null, null, 'Jon', 'Doe', 'INTERPRETER', 31);

insert into translation_language(name)
VALUES ('SPANISH'),
       ('ENGLISH'),
       ('POLISH'),
       ('RUSSIAN'),
       ('GERMAN'),
       ('JAPANESE'),
       ('ASL');

insert into music_style(name)
values ('POP'),
       ('ROCK'),
       ('R''N''B'),
       ('TECHNO'),
       ('A CAPELLA'),
       ('PUNK'),
       ('JAZZ'),
       ('SOUL'),
       ('COVER'),
       ('ELECTRONIC');


insert into service_music_style (id_optional_service, id_music_style)
VALUES (1, 1),
       (1, 5),
       (1, 8),
       (8, 2),
       (8, 6),
       (3, 1),
       (3, 3),
       (5, 9),
       (7, 8),
       (7, 7),
       (2, 5),
       (2, 10);

insert into service_translator_language (id_optional_service, id_language)
VALUES (4, 7),
       (4, 2),
       (10, 1),
       (10, 5),
       (10, 7);

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


insert into service_review(title, comment, star_rating, id_optional_service, id_customer, created_at)
values ('title1', 'comment1', 5, 1, 9, (select current_timestamp)),
       ('title2', 'comment2', 5, 2, 10, (select current_timestamp)),
       ('title3', 'comment3', 5, 3, 11, (select current_timestamp)),
       ('title4', 'comment4', 5, 4, 12, (select current_timestamp)),
       ('title5', 'comment5', 5, 5, 13, (select current_timestamp)),
       ('title6', 'comment6', 5, 6, 9, (select current_timestamp)),
       ('title7', 'comment7', 5, 7, 10, (select current_timestamp)),
       ('title8', 'comment8', 5, 8, 11, (select current_timestamp)),
       ('title9', 'comment9', 5, 9, 12, (select current_timestamp)),
       ('title10', 'comment10', 4, 10, 13, (select current_timestamp));


insert into catering_review(title, comment, id_customer, id_catering, star_rating, created_at)
values ('title1', 'comment1', 9, 1, 1, (select current_timestamp)),
       ('title2', 'comment2', 10, 2, 2, (select current_timestamp)),
       ('title3', 'comment3', 11, 3, 3, (select current_timestamp)),
       ('title4', 'comment4', 12, 4, 4, (select current_timestamp)),
       ('title5', 'comment5', 13, 5, 5, (select current_timestamp)),
       ('title6', 'comment6', 9, 1, 4, (select current_timestamp)),
       ('title7', 'comment7', 10, 2, 5, (select current_timestamp)),
       ('title8', 'comment8', 11, 3, 5, (select current_timestamp)),
       ('title9', 'comment9', 12, 4, 4, (select current_timestamp)),
       ('title10', 'comment10', 13, 5, 2, (select current_timestamp));


INSERT INTO location_review (title, comment, id_location, id_customer, created_at, star_rating)
VALUES ('False advertisement', 'Location was alright but it simply was not as big as advertised.', 1, 9,
        (select current_timestamp), 5),
       ('Great place!', 'I cannot recommend it enough. I love it, will come again.', 2, 10, (select current_timestamp),
        5),
       ('Horrible shack.', 'Location was alright but not fit to my tastes.', 3, 11, (select current_timestamp), 5),
       ('It was so-so', 'It was ok, but not great.', 4, 12, (select current_timestamp), 5),
       ('Just perfect.', 'Clean, spacious and with great customer service. You will not be dissapointed!', 5, 13,
        (select current_timestamp), 5);


INSERT INTO event_type (type)
VALUES ('WEDDING'),
       ('CONFERENCE'),
       ('SEMINAR'),
       ('BIRTHDAY'),
       ('FAMILY_CELEBRATION'),
       ('PARTY'),
       ('OTHER');

INSERT INTO organized_event (event_date, start_time, end_time, event_status, id_customer, id_event_type, created_at,
                             modified_at, deleted_at, name, guest_count)
VALUES ('2021-03-23', '15:00:00', '20:00:00', 'READY', 9, 1, (select current_timestamp), (select current_timestamp),
        null,
        'CELEBRATION', 10),
       ('2021-03-29', '15:00:00', '20:00:00', 'IN_PLANNING', 10, 3, (select current_timestamp),
        (select current_timestamp), null,
        'CELEBRATION', 10),
       ('2021-04-30', '15:00:00', '20:00:00', 'IN_PLANNING', 11, 5, (select current_timestamp),
        (select current_timestamp), null,
        'CELEBRATION', 10),
       ('2021-05-05', '15:00:00', '20:00:00', 'FINISHED', 12, 2, (select current_timestamp), (select current_timestamp),
        null,
        'CELEBRATION', 10),
       ('2021-05-01', '15:00:00', '20:00:00', 'FINISHED', 13, 5, (select current_timestamp), (select current_timestamp),
        null,
        'CELEBRATION', 10);


insert into location_for_event(time_from, time_to, id_organized_event, id_location, guests, confirmation_status)
values ('10:00:00', '21:59:59', 1, 1, 10, 'CONFIRMED'),
       ('10:00:00', '21:59:59', 2, 2, 10, 'CONFIRMED'),
       ('10:00:00', '21:59:59', 3, 3, 10, 'CONFIRMED'),
       ('10:00:00', '21:59:59', 4, 3, 10, 'CONFIRMED'),
       ('10:00:00', '21:59:59', 5, 4, 10, 'CONFIRMED');


insert into service_for_event(time_from, time_to, comment, id_optional_service, id_location_for_event,
                              confirmation_status)
values ('2021-09-01 12:00:00', '2021-09-01 12:00:00', 'comment1', 1, 1, 'CONFIRMED'),
       ('2021-09-02 12:00:00', '2021-09-02 12:00:00', 'comment2', 2, 2, 'CONFIRMED'),
       ('2021-09-03 12:00:00', '2021-09-03 12:00:00', 'comment3', 3, 3, 'CONFIRMED'),
       ('2021-09-04 12:00:00', '2021-09-04 12:00:00', 'comment4', 4, 4, 'CONFIRMED'),
       ('2021-09-05 12:00:00', '2021-09-05 12:00:00', 'comment5', 5, 5, 'CONFIRMED');


insert into catering_for_chosen_location (order_date, order_time, id_catering, id_location_for_event, comment,
                                          confirmation_status)
VALUES ('2021-09-01', '12:00:00', 4, 4, 'comment', 'CONFIRMED');

insert into catering_order_choice (id_catering_item, count, id_catering_for_chosen_location)
VALUES (5, 3, 1),
       (6, 3, 1);


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
