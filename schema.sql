 ============================================================
--  Food Ordering System – Database Schema
-- ============================================================
 
CREATE DATABASE IF NOT EXISTS food_ordering_db;
USE food_ordering_db;
 
-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    full_name  VARCHAR(100) NOT NULL,
    phone      VARCHAR(15),
    address    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 
-- Menu categories
CREATE TABLE IF NOT EXISTS categories (
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);
 
-- Menu items
CREATE TABLE IF NOT EXISTS menu_items (
    item_id     INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    category_id INT,
    available   TINYINT(1) DEFAULT 1,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
 
-- Orders
CREATE TABLE IF NOT EXISTS orders (
    order_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT  NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status       VARCHAR(20) DEFAULT 'Placed',
    order_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
 
-- Order details
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id      INT  NOT NULL,
    item_id       INT  NOT NULL,
    quantity      INT  NOT NULL,
    price         DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (item_id)  REFERENCES menu_items(item_id)
);
 
-- ============================================================
--  Seed Data
-- ============================================================
 
INSERT INTO categories (category_name) VALUES
    ('Starters'),
    ('Main Course'),
    ('Beverages'),
    ('Desserts');
 
INSERT INTO menu_items (name, description, price, category_id) VALUES
    ('Spring Rolls',        'Crispy vegetable spring rolls (2 pcs)',  80.00,  1),
    ('Paneer Tikka',        'Grilled cottage cheese with spices',    150.00,  1),
    ('Garlic Bread',        'Toasted bread with garlic butter',       60.00,  1),
    ('Veg Fried Rice',      'Stir-fried rice with vegetables',       120.00,  2),
    ('Butter Chicken',      'Creamy tomato-based chicken curry',     200.00,  2),
    ('Paneer Butter Masala','Cottage cheese in rich gravy',          180.00,  2),
    ('Dal Makhani',         'Slow-cooked black lentils',             140.00,  2),
    ('Chicken Biryani',     'Aromatic basmati rice with chicken',    220.00,  2),
    ('Cold Coffee',         'Chilled blended coffee',                 80.00,  3),
    ('Mango Lassi',         'Yogurt-based mango drink',               70.00,  3),
    ('Fresh Lime Soda',     'Lime juice with soda',                   50.00,  3),
    ('Gulab Jamun',         'Soft milk dumplings in sugar syrup (2 pcs)', 60.00, 4),
    ('Ice Cream',           'Vanilla / Chocolate scoop',              50.00,  4);
 
-- Default admin user  (password: admin123)
INSERT INTO users (username, password, email, full_name, phone) VALUES
    ('admin', 'admin123', 'admin@foodorder.com', 'Administrator', '9999999999');
 
