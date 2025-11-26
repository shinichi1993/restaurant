package com.restaurant.api.repository;

import com.restaurant.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository thao tác với bảng orders.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Sau này có thể bổ sung filter theo ngày, theo bàn, theo trạng thái...
}
