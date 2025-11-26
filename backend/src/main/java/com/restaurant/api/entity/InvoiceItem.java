package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity InvoiceItem – chứa chi tiết món ăn trong hóa đơn.
 * Đây là snapshot từ OrderItem, để đảm bảo in lại hóa đơn không thay đổi.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoice_item")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên món snapshot tại thời điểm tạo hóa đơn
    @Column(nullable = false)
    private String name;

    // Giá snapshot (không lấy từ dish nữa)
    @Column(nullable = false)
    private Double price;

    // Số lượng món ăn
    @Column(nullable = false)
    private Integer quantity;

    // Khóa ngoại tới invoice
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}
