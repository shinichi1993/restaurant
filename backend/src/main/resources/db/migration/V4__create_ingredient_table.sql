-- ============================================
-- V3 – Tạo bảng ingredient cho Module 03
-- Quản lý nguyên liệu
-- ============================================

CREATE TABLE ingredient (
    id BIGSERIAL PRIMARY KEY,                -- Khóa chính
    name VARCHAR(255) NOT NULL,              -- Tên nguyên liệu
    unit VARCHAR(50) NOT NULL,               -- Đơn vị tính (gram, ml, cái...)
    stock_quantity NUMERIC(12, 2) DEFAULT 0, -- Tồn kho hiện tại
    min_stock NUMERIC(12, 2) DEFAULT 0,      -- Ngưỡng cảnh báo
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), -- Thời điểm tạo
    created_by VARCHAR(100)                  -- Người tạo (username)
);

-- Index để tìm kiếm nhanh theo tên nguyên liệu
CREATE INDEX idx_ingredient_name ON ingredient(name);
