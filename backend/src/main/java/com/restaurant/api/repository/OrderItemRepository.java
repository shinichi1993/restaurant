package com.restaurant.api.repository;

import com.restaurant.api.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository thao tác với bảng order_item.
 * Hiện tại chủ yếu dùng qua Order (cascade), nhưng vẫn tạo để linh hoạt về sau.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
