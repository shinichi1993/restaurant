package com.restaurant.api.controller;

import com.restaurant.api.dto.category.CategoryCreateRequest;
import com.restaurant.api.dto.category.CategoryResponse;
import com.restaurant.api.dto.category.CategoryUpdateRequest;
import com.restaurant.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CategoryController
 * - Định nghĩa API CRUD cho danh mục món
 * - Base URL: /api/categories
 * - Không phân quyền (theo Module 2)
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * API tạo danh mục mới
     * POST /api/categories
     */
    @PostMapping
    public CategoryResponse create(@RequestBody CategoryCreateRequest request) {
        return categoryService.create(request);
    }

    /**
     * API lấy toàn bộ danh mục
     * GET /api/categories
     */
    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAll();
    }

    /**
     * API cập nhật danh mục theo ID
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable Long id,
            @RequestBody CategoryUpdateRequest request
    ) {
        return categoryService.update(id, request);
    }

    /**
     * API xóa danh mục theo ID
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
