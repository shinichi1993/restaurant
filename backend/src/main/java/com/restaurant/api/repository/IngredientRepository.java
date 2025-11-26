package com.restaurant.api.repository;

import com.restaurant.api.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository cho entity Ingredient
 * Hỗ trợ CRUD mặc định từ JpaRepository.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    // Tìm theo tên nguyên liệu (dùng cho kiểm tra trùng tên)
    boolean existsByName(String name);
}
