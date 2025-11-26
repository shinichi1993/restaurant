package com.restaurant.api.dto.dish;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO trả về thông tin món ăn
 */
@Getter
@Setter
public class DishResponse {

    private Long id;

    /** Tên món */
    private String name;

    /** Mô tả */
    private String description;

    /** Giá */
    private BigDecimal price;

    /** Link ảnh */
    private String imageUrl;

    /** ID danh mục */
    private Long categoryId;

    /** Tên danh mục (để hiển thị FE) */
    private String categoryName;

    /** Ngày tạo */
    private LocalDateTime createdAt;

    /** Ngày cập nhật */
    private LocalDateTime updatedAt;
}
