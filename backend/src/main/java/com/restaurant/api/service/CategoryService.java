package com.restaurant.api.service;

import com.restaurant.api.dto.category.CategoryCreateRequest;
import com.restaurant.api.dto.category.CategoryResponse;
import com.restaurant.api.dto.category.CategoryUpdateRequest;

import java.util.List;

/**
 * CategoryService (Interface)
 * Định nghĩa các chức năng CRUD cho danh mục món.
 */
public interface CategoryService {

    // Tạo danh mục mới
    CategoryResponse create(CategoryCreateRequest request);

    // Lấy danh sách tất cả danh mục
    List<CategoryResponse> getAll();

    // Cập nhật danh mục theo ID
    CategoryResponse update(Long id, CategoryUpdateRequest request);

    // Xóa danh mục theo ID
    void delete(Long id);
}
