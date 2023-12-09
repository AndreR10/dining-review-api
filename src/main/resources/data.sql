INSERT INTO user_account (name, city, state, zip_Code, is_admin, interested_In_Peanut_Allergies, interested_In_Egg_Allergies, interested_In_Dairy_Allergies) 
VALUES 
('Zeboode', 'Lisbon', 'Mafra', '2640-741', false, false, false, false),
('ZeManco', 'Lisbon', 'Torres Vedras', '2640-741', false, false, true, true),
('ZeMaluco', 'Lisbon', 'Malveira', '2640-741', false, true, true, true);

INSERT INTO restaurant (name, zip_Code) 
VALUES ('Restaurant1', '1222-345'),
('Restaurant2', '5432-221'),
('Restaurant3', '9872-265');

INSERT INTO review (reviewer_Name, restaurant_Id, peanut_Score, egg_Score, dairy_Score, commentary, status)
VALUES 
('ZeManco', 1, 4.5, 3.0, 4.8, 'Great experience!', 'PENDING'),
('Zeboode', 1, 3.8, 2.5, 3.5, 'Could be better', 'PENDING'),
('ZeMaluco', 3, 4.2, 4.0, 4.5, 'No complaints', 'PENDING');