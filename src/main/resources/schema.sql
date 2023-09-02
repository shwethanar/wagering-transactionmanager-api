-- Create the CUSTOMER table
CREATE TABLE CUSTOMER (
    customer_id INT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    age INT,
    location VARCHAR(255)
);

-- Create the PRODUCT table
CREATE TABLE PRODUCT (
    product_code VARCHAR(20) PRIMARY KEY,
    cost DECIMAL(10, 2),
    status VARCHAR(20)
);

-- Create the TRANSACTION table
-- Create the TRANSACTION table
CREATE TABLE TRANSACTION (
    id SERIAL PRIMARY KEY, -- Use SERIAL for auto-incrementing primary key
    transaction_time TIMESTAMP,
    customer_id BIGINT, -- Use BIGINT for a Long type
    quantity INT,
    product_code VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id),
    FOREIGN KEY (product_code) REFERENCES PRODUCT(product_code)
);
