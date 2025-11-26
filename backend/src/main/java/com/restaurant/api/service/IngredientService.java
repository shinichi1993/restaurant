package com.restaurant.api.service;

import com.restaurant.api.dto.ingredient.IngredientRequest;
import com.restaurant.api.dto.ingredient.IngredientResponse;
import com.restaurant.api.entity.Ingredient;
import com.restaurant.api.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ cho nguyên liệu (Ingredient)
 * KHÔNG dùng Auditable, KHÔNG dùng SecurityUtils, KHÔNG dùng DateUtils.
 */
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    /**
     * Lấy danh sách tất cả nguyên liệu
     */
    public List<IngredientResponse> getAll() {
        return ingredientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Tạo mới nguyên liệu
     */
    @Transactional
    public IngredientResponse create(IngredientRequest req) {

        if (ingredientRepository.existsByName(req.getName())) {
            throw new RuntimeException("Tên nguyên liệu đã tồn tại");
        }

        Ingredient ingredient = Ingredient.builder()
                .name(req.getName())
                .unit(req.getUnit())
                .stockQuantity(req.getStockQuantity())
                .minStock(req.getMinStock())
                .createdAt(LocalDateTime.now())
                .createdBy("admin") // Tạm thời cứng vì Project – Quán ăn không có audit
                .build();

        ingredientRepository.save(ingredient);

        return toResponse(ingredient);
    }

    /**
     * Cập nhật nguyên liệu
     */
    @Transactional
    public IngredientResponse update(Long id, IngredientRequest req) {

        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu"));

        // Kiểm tra trùng tên
        if (!ingredient.getName().equals(req.getName())) {
            if (ingredientRepository.existsByName(req.getName())) {
                throw new RuntimeException("Tên nguyên liệu đã tồn tại");
            }
        }

        ingredient.setName(req.getName());
        ingredient.setUnit(req.getUnit());
        ingredient.setStockQuantity(req.getStockQuantity());
        ingredient.setMinStock(req.getMinStock());

        ingredientRepository.save(ingredient);

        return toResponse(ingredient);
    }

    /**
     * Xoá nguyên liệu
     */
    @Transactional
    public void delete(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy nguyên liệu");
        }
        ingredientRepository.deleteById(id);
    }

    /**
     * Mapping entity → DTO
     */
    private IngredientResponse toResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .unit(ingredient.getUnit())
                .stockQuantity(ingredient.getStockQuantity())
                .minStock(ingredient.getMinStock())
                .createdAt(ingredient.getCreatedAt().toString()) // Trả raw string
                .createdBy(ingredient.getCreatedBy())
                .build();
    }
}
