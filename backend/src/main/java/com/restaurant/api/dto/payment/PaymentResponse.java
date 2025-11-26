package com.restaurant.api.dto.payment;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO trả dữ liệu payment về cho FE.
 * Gồm cả thông tin liên quan đến Invoice (mà Payment đang tham chiếu).
 */
@Getter
@Builder
public class PaymentResponse {

    private Long id;               // ID payment
    private Long invoiceId;        // ID invoice
    private String invoiceCode;    // Mã hóa đơn (Invoice.code)
    private Long orderId;          // ID đơn hàng (Invoice.orderId)

    private BigDecimal amount;     // Số tiền thanh toán
    private String method;         // Phương thức thanh toán
    private String status;         // Trạng thái thanh toán
    private String transactionCode;// Mã giao dịch

    private LocalDateTime createdAt; // Thời điểm tạo payment
    private LocalDateTime paidAt;    // Thời điểm invoice được đánh dấu đã thanh toán
}
