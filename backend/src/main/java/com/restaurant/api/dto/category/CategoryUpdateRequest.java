package com.restaurant.api.dto.category;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng khi cập nhật danh mục
 * Không cho chỉnh ngày tạo
 */
@Getter
@Setter
public class CategoryUpdateRequest {

    // Tên danh mục
    private String name;

    // Mô tả danh mục
    private String description;
}
