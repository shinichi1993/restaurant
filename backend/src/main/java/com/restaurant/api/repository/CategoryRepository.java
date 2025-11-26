package com.restaurant.api.repository;

import com.restaurant.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository cho Category
 * Kế thừa JpaRepository để có toàn bộ CRUD mặc định.
 * Không tự viết query nếu chưa cần thiết.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Ví dụ nếu sau này cần check trùng tên danh mục:
    boolean existsByName(String name);
}
