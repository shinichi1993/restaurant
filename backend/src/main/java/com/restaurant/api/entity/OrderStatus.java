package com.restaurant.api.entity;

/**
 * Enum trạng thái đơn hàng.
 * - PENDING: Mới tạo, chưa chế biến, chưa thanh toán
 * - COOKING: Đang chế biến
 * - DONE: Đã chế biến xong (đã phục vụ)
 * - CANCELLED: Đã hủy
 * - PAID: Đã thanh toán (tự động tạo hóa đơn)
 */
public enum OrderStatus {
    PENDING,
    COOKING,
    DONE,
    CANCELLED,
    PAID // Trạng thái sau khi thanh toán
}
