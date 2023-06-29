INSERT INTO restaurant.restaurants(id, name, active)
VALUES ("9cd934e8-f060-4b71-8e04-abac2adec86f", "restaurant_1", TRUE);
INSERT INTO restaurant.restaurants(id, name, active)
VALUES ("7424b249-30ba-4c89-b2bc-35e84142cc12", "restaurant_2", TRUE);

INSERT INTO restaurant.products(id, name, price, available)
VALUES ("6580f8ba-7308-4541-b808-3b57d60a7c4e", "product_1", 25.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES ("32a051ba-b7cb-47d8-8cda-18280c94025a", "product_2", 50.00, TRUE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES ("da9fb056-e4d8-4f15-8ae5-514fd71ec49b", "product_3", 20.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES ("de3c2eb9-a182-4bb4-b11f-6cfbb3c868ef", "product_4", 40.00, TRUE);

INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ("3b6ed3b3-09e1-4aac-ac59-9117bcdcecf6", "9cd934e8-f060-4b71-8e04-abac2adec86f",
        "6580f8ba-7308-4541-b808-3b57d60a7c4e");
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ("f02ac097-77ce-4c3a-bdb1-60881420c053", "9cd934e8-f060-4b71-8e04-abac2adec86f",
        "32a051ba-b7cb-47d8-8cda-18280c94025a");
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ("8ee1e63a-f5a1-4a77-ad86-c75eac174449", "7424b249-30ba-4c89-b2bc-35e84142cc12",
        "da9fb056-e4d8-4f15-8ae5-514fd71ec49b");
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ("62cf3264-7489-4ba4-877b-2eccacde965b", "7424b249-30ba-4c89-b2bc-35e84142cc12",
        "de3c2eb9-a182-4bb4-b11f-6cfbb3c868ef");