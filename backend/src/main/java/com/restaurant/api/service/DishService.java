package com.restaurant.api.service;

import com.restaurant.api.dto.dish.DishCreateRequest;
import com.restaurant.api.dto.dish.DishUpdateRequest;
import com.restaurant.api.dto.dish.DishResponse;

import java.util.List;

/**
 * DishService – Định nghĩa các chức năng CRUD cho Dish
 */
public interface DishService {

    /** Tạo món ăn mới */
    DishResponse create(DishCreateRequest request);

    /** Lấy danh sách món ăn */
    List<DishResponse> getAll();

    /** Lấy danh sách theo category */
    List<DishResponse> getByCategory(Long categoryId);

    /** Cập nhật món ăn theo ID */
    DishResponse update(Long id, DishUpdateRequest request);

    /** Xoá món ăn theo ID */
    void delete(Long id);
}
