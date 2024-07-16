CREATE TABLE Categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feature_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) UNIQUE NOT NULL,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    quantity_available INT NOT NULL,
    weight FLOAT NOT NULL,
    rating FLOAT NOT NULL,
    category_id BIGINT,
    description TEXT,
    color VARCHAR(50) NOT NULL,
    price FLOAT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Categories(id)
);

CREATE TABLE Photos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    photo_name VARCHAR(255) UNIQUE NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE products_features (
    product_id BIGINT,
    feature_id BIGINT,
    PRIMARY KEY (product_id, feature_id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (feature_id) REFERENCES Features(id)
);

