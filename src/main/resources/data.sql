INSERT INTO Categories (category_name) VALUES ('Electronics');
INSERT INTO Categories (category_name) VALUES ('Home Appliances');
INSERT INTO Categories (category_name) VALUES ('Furniture');
INSERT INTO Categories (category_name) VALUES ('Sports');

INSERT INTO Features (feature_name) VALUES ('Noise Cancellation');
INSERT INTO Features (feature_name) VALUES ('Wireless');
INSERT INTO Features (feature_name) VALUES ('Bluetooth');
INSERT INTO Features (feature_name) VALUES ('Foldable');
INSERT INTO Features (feature_name) VALUES ('Microphone');
INSERT INTO Features (feature_name) VALUES ('Water Resistant');

INSERT INTO Products (product_name, brand, model, quantity_available, weight, rating, category_id, description, color, price)
VALUES
('Headphones Acme', 'Acme', 'X123', 50, 0.5, 4.5, 1, 'High quality headphones with excellent sound', 'Black', 99.99),
('Wireless Earbuds', 'SoundMagic', 'E10', 100, 0.1, 4.7, 1, 'Compact and comfortable wireless earbuds', 'White', 79.99),
('Refrigerator', 'LG', 'GR-B202SQBB', 20, 70, 4.8, 2, 'Spacious refrigerator with energy saving features', 'Silver', 499.99),
('Office Chair', 'Ikea', 'Markus', 30, 15, 4.3, 3, 'Comfortable office chair with adjustable height', 'Black', 199.99),
('Running Shoes', 'Nike', 'Air Zoom Pegasus', 70, 0.8, 4.9, 4, 'Comfortable running shoes with excellent cushioning', 'Blue', 119.99);

INSERT INTO Photos (product_id, photo_name, photo_url)
VALUES
(1, 'headphones', 'https://i.imgur.com/ZQ3E3xz.jpeg'),
(2, 'earbuds', 'https://i.imgur.com/y2f67pn.png'),
(3, 'refrigerator', 'https://i.imgur.com/i7krYhb.jpeg'),
(4, 'chair', 'https://i.imgur.com/6USaT9g.png'),
(5, 'shoes', 'https://i.imgur.com/N4tZgul.png');

INSERT INTO products_features (product_id, feature_id) VALUES (1, 1);
INSERT INTO products_features (product_id, feature_id) VALUES (1, 4);
INSERT INTO products_features (product_id, feature_id) VALUES (1, 5);
INSERT INTO products_features (product_id, feature_id) VALUES (2, 2);
INSERT INTO products_features (product_id, feature_id) VALUES (2, 3);
INSERT INTO products_features (product_id, feature_id) VALUES (3, 6);
INSERT INTO products_features (product_id, feature_id) VALUES (4, 3);
INSERT INTO products_features (product_id, feature_id) VALUES (5, 6);