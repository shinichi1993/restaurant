package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity Dish – Món ăn
 * Lưu thông tin món ăn và liên kết với Category
 */
@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dish {

    /** ID tự tăng */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tên món ăn */
    @Column(nullable = false)
    private String name;

    /** Mô tả món ăn */
    private String description;

    /** Giá món ăn */
    @Column(nullable = false)
    private BigDecimal price;

    /** URL ảnh món ăn */
    private String imageUrl;

    /** Category chứa món ăn (FK) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** Thời gian tạo */
    private LocalDateTime createdAt;

    /** Thời gian cập nhật */
    private LocalDateTime updatedAt;

    /** Tự set createdAt khi tạo mới */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /** Tự set updatedAt khi update */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
