package com.restaurant.api.dto.order;

import lombok.Data;

import java.util.List;

/**
 * DTO tạo mới đơn hàng.
 * FE sẽ gửi JSON theo cấu trúc này.
 */
@Data
public class OrderCreateRequest {

    private String customerName;   // Tên khách

    private String customerPhone;  // SĐT khách

    private String tableNumber;    // Số bàn

    private String note;           // Ghi chú

    private List<OrderItemRequest> items; // Danh sách món trong đơn
}
