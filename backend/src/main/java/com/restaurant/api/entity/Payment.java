package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity Payment - Lưu thông tin thanh toán cho hóa đơn (Invoice).
 * Quan hệ 1-1 với Invoice:
 * - Mỗi invoice có tối đa 1 payment.
 * - Payment là "bằng chứng" invoice đã được thanh toán hay chưa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính

    /**
     * Quan hệ 1-1 với Invoice.
     * - Chỉ lưu FK ở phía Payment để tránh phải sửa lại entity Invoice hiện tại.
     * - Khi cần thông tin invoice (code, orderId, paidAt...) thì truy ngược qua thuộc tính này.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false, unique = true)
    private Invoice invoice;

    @Column(nullable = false)
    private BigDecimal amount; // Số tiền thanh toán

    @Column(nullable = false, length = 50)
    private String method; // Phương thức thanh toán (CASH, MOMO, BANK,...)

    @Column(nullable = false, length = 50)
    private String status; // Trạng thái thanh toán (SUCCESS, FAILED, PENDING)

    @Column(name = "transaction_code", length = 100)
    private String transactionCode; // Mã giao dịch từ cổng thanh toán

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời điểm tạo payment
}
