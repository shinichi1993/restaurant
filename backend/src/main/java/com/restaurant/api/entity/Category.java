package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entity Category – bảng category
 * Dùng cho Module 2: Quản lý danh mục món
 */
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên danh mục
    @Column(nullable = false)
    private String name;

    // Mô tả danh mục
    @Column(columnDefinition = "TEXT")
    private String description;

    // Ngày tạo
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Ngày cập nhật
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
