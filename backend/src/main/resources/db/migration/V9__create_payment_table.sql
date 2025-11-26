-- V9__create_payment_table.sql
-- Tạo bảng payment để lưu thông tin thanh toán cho invoice
-- Quan hệ: 1 payment ↔ 1 invoice

CREATE TABLE payment (
    id BIGSERIAL PRIMARY KEY,           -- Khóa chính
    invoice_id BIGINT NOT NULL,         -- FK tới bảng invoice
    amount NUMERIC(15, 2) NOT NULL,     -- Số tiền thanh toán (thường = tổng tiền invoice)
    method VARCHAR(50) NOT NULL,        -- Phương thức thanh toán (CASH, MOMO, BANK, ...)
    status VARCHAR(50) NOT NULL,        -- Trạng thái thanh toán (SUCCESS, FAILED, PENDING)
    transaction_code VARCHAR(100),      -- Mã giao dịch (từ Momo, ngân hàng...)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Thời điểm tạo payment

    CONSTRAINT fk_payment_invoice
        FOREIGN KEY (invoice_id)
        REFERENCES invoice (id)
        ON DELETE CASCADE
);

-- Index hỗ trợ join/filter nhanh theo invoice và thời gian
CREATE INDEX idx_payment_invoice_id ON payment (invoice_id);
CREATE INDEX idx_payment_created_at ON payment (created_at);
