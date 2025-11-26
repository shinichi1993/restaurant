package com.restaurant.api.service.impl;

import com.restaurant.api.dto.dish.DishCreateRequest;
import com.restaurant.api.dto.dish.DishResponse;
import com.restaurant.api.dto.dish.DishUpdateRequest;
import com.restaurant.api.entity.Category;
import com.restaurant.api.entity.Dish;
import com.restaurant.api.repository.CategoryRepository;
import com.restaurant.api.repository.DishRepository;
import com.restaurant.api.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DishServiceImpl – Xử lý nghiệp vụ cho món ăn
 */
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public DishResponse create(DishCreateRequest request) {

        // Lấy category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        // Tạo entity
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setImageUrl(request.getImageUrl());
        dish.setCategory(category);

        // Lưu
        Dish saved = dishRepository.save(dish);

        return toResponse(saved);
    }

    @Override
    public List<DishResponse> getAll() {
        return dishRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishResponse> getByCategory(Long categoryId) {
        return dishRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DishResponse update(Long id, DishUpdateRequest request) {

        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));

        // Lấy category mới (nếu có)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        // Cập nhật field
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setImageUrl(request.getImageUrl());
        dish.setCategory(category);

        // Lưu lại
        Dish updated = dishRepository.save(dish);

        return toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        dishRepository.deleteById(id);
    }

    /**
     * Convert entity → response DTO
     */
    private DishResponse toResponse(Dish dish) {
        DishResponse dto = new DishResponse();

        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setImageUrl(dish.getImageUrl());
        dto.setCategoryId(dish.getCategory().getId());
        dto.setCategoryName(dish.getCategory().getName());
        dto.setCreatedAt(dish.getCreatedAt());
        dto.setUpdatedAt(dish.getUpdatedAt());

        return dto;
    }
}
