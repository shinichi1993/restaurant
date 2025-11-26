-- Tạo bảng orders để lưu thông tin đơn hàng
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    customer_name VARCHAR(255),
    customer_phone VARCHAR(50),
    table_number VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    total_amount NUMERIC(18, 2) NOT NULL,
    note TEXT,
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Tạo bảng order_item để lưu chi tiết món của từng đơn hàng
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(18, 2) NOT NULL,
    amount NUMERIC(18, 2) NOT NULL,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,

    CONSTRAINT fk_order_item_dish
        FOREIGN KEY (dish_id) REFERENCES dish (id)
);
