package com.restaurant.api.dto.category;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng khi tạo danh mục mới
 * Chỉ chứa field người dùng được phép nhập
 */
@Getter
@Setter
public class CategoryCreateRequest {

    // Tên danh mục
    private String name;

    // Mô tả danh mục
    private String description;
}
