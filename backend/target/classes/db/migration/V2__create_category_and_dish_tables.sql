-- ============================================
-- V3 - Tạo bảng category cho Module 2
-- ============================================

CREATE TABLE category (
    id SERIAL PRIMARY KEY,

    -- Tên danh mục (bắt buộc)
    name VARCHAR(255) NOT NULL,

    -- Mô tả (không bắt buộc)
    description TEXT,

    -- Ngày tạo / cập nhật
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Index để tìm kiếm nhanh theo tên
CREATE INDEX idx_category_name ON category(name);
