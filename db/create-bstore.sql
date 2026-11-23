-- SQL script to create the required DB for BSTORE
CREATE DATABASE IF NOT EXISTS bstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- optionally create a DB user with restricted permissions
-- CREATE USER 'bstore_user'@'localhost' IDENTIFIED BY 'your_secure_password';
-- GRANT ALL PRIVILEGES ON bstore.* TO 'bstore_user'@'localhost';
-- FLUSH PRIVILEGES;
