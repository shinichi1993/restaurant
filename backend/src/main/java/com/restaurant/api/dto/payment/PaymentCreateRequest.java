package com.restaurant.api.dto.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO dùng khi tạo mới một payment cho invoice.
 * Tạm thời đơn giản:
 * - amount: thường = số tiền invoice
 * - method, status, transactionCode sẽ do BE/cổng thanh toán cung cấp.
 */
@Getter
@Setter
public class PaymentCreateRequest {

    private BigDecimal amount;      // Số tiền thanh toán
    private String method;          // Phương thức thanh toán
    private String status;          // Trạng thái (SUCCESS, FAILED, PENDING)
    private String transactionCode; // Mã giao dịch (nếu có)
}
