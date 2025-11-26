package com.restaurant.api.repository;

import com.restaurant.api.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho Dish
 * Chỉ thao tác CRUD cơ bản và query theo category
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    /**
     * Lấy danh sách món theo category (dùng cho FE lọc)
     */
    List<Dish> findByCategoryId(Long categoryId);
}
