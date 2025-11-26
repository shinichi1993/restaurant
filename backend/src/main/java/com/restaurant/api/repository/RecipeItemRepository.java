package com.restaurant.api.repository;

import com.restaurant.api.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository thao tác với bảng recipe_item
 */
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {

    /**
     * Lấy danh sách định lượng theo id món ăn.
     * Mỗi RecipeItem = 1 nguyên liệu + số lượng cần cho 1 phần món.
     */
    List<RecipeItem> findByDishId(Long dishId);

    /**
     * Kiểm tra xem 1 nguyên liệu đã được cấu hình trong món này chưa
     * Dùng để tránh tạo trùng
     */
    boolean existsByDishIdAndIngredientId(Long dishId, Long ingredientId);
}
