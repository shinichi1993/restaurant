package com.restaurant.api.dto.order;

import lombok.Data;

/**
 * DTO cho 1 món trong yêu cầu tạo/cập nhật đơn.
 */
@Data
public class OrderItemRequest {

    private Long dishId;   // ID món ăn được chọn trên FE

    private Integer quantity; // Số lượng đặt
}
