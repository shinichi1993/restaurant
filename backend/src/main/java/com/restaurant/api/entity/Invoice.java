package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity Invoice – đại diện cho một hóa đơn.
 * Snapshot dữ liệu từ Order tại thời điểm xuất hóa đơn.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã hóa đơn – sinh tự động dạng INV0001...
    @Column(nullable = false)
    private String code;

    // Tham chiếu tới Order gốc
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    // Tổng tiền
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    // Trạng thái: PAID / UNPAID
    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // Quan hệ 1-n tới InvoiceItem
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> items;
}
