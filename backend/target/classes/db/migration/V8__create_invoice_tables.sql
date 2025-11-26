-- V8: Tạo bảng invoice và invoice_item cho chức năng quản lý hóa đơn
-- Mô-đun: Module 07 – Invoice

CREATE TABLE invoice (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,              -- Mã hóa đơn
    total_amount NUMERIC(12,2) NOT NULL,     -- Tổng tiền
    status VARCHAR(20) NOT NULL,             -- Trạng thái: PAID / UNPAID
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,        -- Người tạo hóa đơn
    paid_at TIMESTAMP NULL,

    CONSTRAINT fk_invoice_order
        FOREIGN KEY(order_id) REFERENCES orders(id)
);

CREATE INDEX idx_invoice_code ON invoice(code);
CREATE INDEX idx_invoice_created_at ON invoice(created_at);


-- Bảng chi tiết hóa đơn: snapshot món ăn tại thời điểm xuất hóa đơn.
CREATE TABLE invoice_item (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,              -- Tên món (snapshot)
    price NUMERIC(12,2) NOT NULL,            -- Đơn giá tại thời điểm tạo
    quantity INT NOT NULL,                   -- Số lượng

    CONSTRAINT fk_invoice_item_invoice
        FOREIGN KEY(invoice_id) REFERENCES invoice(id)
);

CREATE INDEX idx_invoice_item_invoice ON invoice_item(invoice_id);
