package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity Ingredient – Quản lý nguyên liệu
 * Khớp 100% với bảng ingredient trong Flyway V3.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính

    @Column(nullable = false)
    private String name; // Tên nguyên liệu

    @Column(nullable = false)
    private String unit; // Đơn vị tính (gram, ml, cái...)

    @Column(name = "stock_quantity")
    private Double stockQuantity; // Tồn kho hiện tại

    @Column(name = "min_stock")
    private Double minStock; // Ngưỡng cảnh báo thấp

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Thời điểm tạo

    @Column(name = "created_by")
    private String createdBy; // Người tạo (username)
}
