package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity lưu thông tin phiếu nhập kho.
 * Không cho phép sửa/xóa theo quy định kế toán.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stock_entry")
public class StockEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nguyên liệu được nhập
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    // Số lượng nhập
    private Integer quantity;

    // Ghi chú
    private String note;

    // Người tạo (tạm thời hardcode “admin” theo Rule 54)
    private String createdBy;

    // Thời gian tạo
    private LocalDateTime createdAt;
}
