package com.restaurant.api.controller;

import com.restaurant.api.dto.dish.DishCreateRequest;
import com.restaurant.api.dto.dish.DishResponse;
import com.restaurant.api.dto.dish.DishUpdateRequest;
import com.restaurant.api.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DishController
 * - Cung cấp API CRUD cho món ăn
 * - Chỉ điều hướng request → service xử lý
 */
@RestController
@RequestMapping("/api/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    /**
     * Tạo món ăn mới
     * POST /api/dish
     */
    @PostMapping
    public DishResponse create(@RequestBody DishCreateRequest request) {
        return dishService.create(request);
    }

    /**
     * Lấy danh sách tất cả món ăn
     * GET /api/dish
     */
    @GetMapping
    public List<DishResponse> getAll() {
        return dishService.getAll();
    }

    /**
     * Lấy món ăn theo category
     * GET /api/dish/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public List<DishResponse> getByCategory(@PathVariable Long categoryId) {
        return dishService.getByCategory(categoryId);
    }

    /**
     * Update món ăn theo ID
     * PUT /api/dish/{id}
     */
    @PutMapping("/{id}")
    public DishResponse update(
            @PathVariable Long id,
            @RequestBody DishUpdateRequest request
    ) {
        return dishService.update(id, request);
    }

    /**
     * Xoá món ăn theo ID
     * DELETE /api/dish/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dishService.delete(id);
    }
}
