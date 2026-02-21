-- Drop existing tables if they exist
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS product;

-- Create product table
CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Unique product identifier',
    product_name VARCHAR(255) NOT NULL COMMENT 'Product name',
    created_by VARCHAR(100) NOT NULL COMMENT 'Username who created the product',
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when product was created',
    modified_by VARCHAR(100) COMMENT 'Username who last modified the product',
    modified_on TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Timestamp when product was last modified',
    version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version',
    
    UNIQUE KEY unique_product_name (product_name),
    INDEX idx_product_name (product_name),
    INDEX idx_created_by (created_by),
    INDEX idx_created_on (created_on),
    INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product master table';

-- Create item table
CREATE TABLE item (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Unique item identifier',
    product_id INT NOT NULL COMMENT 'Foreign key reference to product table',
    quantity INT NOT NULL DEFAULT 0 COMMENT 'Item quantity',
    version BIGINT DEFAULT 0 COMMENT 'Optimistic locking version',
    
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_quantity (quantity),
    INDEX idx_version (version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product items table';

-- Sample data insertion (optional)
INSERT INTO product (product_name, created_by) VALUES
('Laptop', 'admin'),
('Desktop Computer', 'admin'),
('Smartphone', 'admin'),
('Tablet', 'admin'),
('Headphones', 'admin');

INSERT INTO item (product_id, quantity) VALUES
(1, 50),
(1, 30),
(2, 20),
(3, 100),
(3, 75),
(4, 45),
(5, 200);

-- Display final state
SELECT COUNT(*) as total_products FROM product;
SELECT COUNT(*) as total_items FROM item;

-- Display product with item count
SELECT 
    p.id,
    p.product_name,
    p.created_by,
    p.created_on,
    COUNT(i.id) as item_count
FROM product p
LEFT JOIN item i ON p.id = i.product_id
GROUP BY p.id, p.product_name, p.created_by, p.created_on
ORDER BY p.created_on DESC;
