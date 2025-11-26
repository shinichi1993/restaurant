package com.restaurant.api.dto.dish;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO tạo món ăn mới
 */
@Getter
@Setter
public class DishCreateRequest {

    /** Tên món ăn */
    private String name;

    /** Mô tả món */
    private String description;

    /** Giá món ăn */
    private BigDecimal price;

    /** Link ảnh (không bắt buộc) */
    private String imageUrl;

    /** ID danh mục chứa món ăn */
    private Long categoryId;
}
