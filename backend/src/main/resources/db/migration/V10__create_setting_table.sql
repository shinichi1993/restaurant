-- Tạo bảng setting để lưu các cấu hình hệ thống
CREATE TABLE setting (
    id BIGSERIAL PRIMARY KEY,
    restaurant_name VARCHAR(255) NOT NULL,
    restaurant_address VARCHAR(255),
    restaurant_phone VARCHAR(50),
    restaurant_email VARCHAR(255),
    logo_url TEXT,

    vat_percent NUMERIC(5,2) DEFAULT 0,
    service_percent NUMERIC(5,2) DEFAULT 0,
    opening_time VARCHAR(10),
    closing_time VARCHAR(10),
    table_count INT DEFAULT 0,

    invoice_header VARCHAR(255),
    invoice_footer VARCHAR(255),

    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
