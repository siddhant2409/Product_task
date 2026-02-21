-- Drop existing tables if they exist
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS product CASCADE;

-- Create product table
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL UNIQUE,
    created_by VARCHAR(100) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(100),
    modified_on TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- Create indexes for product table
CREATE INDEX idx_product_name ON product(product_name);
CREATE INDEX idx_created_by ON product(created_by);
CREATE INDEX idx_created_on ON product(created_on);
CREATE INDEX idx_version ON product(version);

-- Add comment to product table
COMMENT ON TABLE product IS 'Product master table';
COMMENT ON COLUMN product.id IS 'Unique product identifier';
COMMENT ON COLUMN product.product_name IS 'Product name';
COMMENT ON COLUMN product.created_by IS 'Username who created the product';
COMMENT ON COLUMN product.created_on IS 'Timestamp when product was created';
COMMENT ON COLUMN product.modified_by IS 'Username who last modified the product';
COMMENT ON COLUMN product.modified_on IS 'Timestamp when product was last modified';
COMMENT ON COLUMN product.version IS 'Optimistic locking version';

-- Create item table
CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 0,
    version BIGINT DEFAULT 0
);

-- Create indexes for item table
CREATE INDEX idx_product_id ON item(product_id);
CREATE INDEX idx_quantity ON item(quantity);
CREATE INDEX idx_version ON item(version);

-- Add comment to item table
COMMENT ON TABLE item IS 'Product items table';
COMMENT ON COLUMN item.id IS 'Unique item identifier';
COMMENT ON COLUMN item.product_id IS 'Foreign key reference to product table';
COMMENT ON COLUMN item.quantity IS 'Item quantity';
COMMENT ON COLUMN item.version IS 'Optimistic locking version';

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
