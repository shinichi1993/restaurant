-- Tạo bảng lưu phiếu nhập kho (Stock Entry)
CREATE TABLE stock_entry (
    id BIGSERIAL PRIMARY KEY,

    ingredient_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    note TEXT,

    created_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_stock_entry_ingredient
        FOREIGN KEY (ingredient_id)
        REFERENCES ingredient(id)
);
