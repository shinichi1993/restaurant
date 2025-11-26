package com.restaurant.api.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO trả về chi tiết từng món trong đơn.
 */
@Data
@Builder
public class OrderItemResponse {

    private Long id;       // ID của OrderItem

    private Long dishId;   // ID món ăn

    private String dishName; // Tên món (hiển thị cho người dùng)

    private Integer quantity; // Số lượng

    private BigDecimal price; // Giá 1 phần

    private BigDecimal amount; // Thành tiền = quantity * price
}
