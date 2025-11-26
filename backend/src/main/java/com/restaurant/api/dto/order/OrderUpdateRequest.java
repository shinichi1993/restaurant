package com.restaurant.api.dto.order;

import com.restaurant.api.entity.OrderStatus;
import lombok.Data;

import java.util.List;

/**
 * DTO cập nhật đơn hàng.
 * Cho phép chỉnh sửa thông tin khách, trạng thái và danh sách món.
 */
@Data
public class OrderUpdateRequest {

    private String customerName;

    private String customerPhone;

    private String tableNumber;

    private String note;

    private OrderStatus status; // Trạng thái mới

    private List<OrderItemRequest> items; // Danh sách món mới (nếu FE cho phép sửa)
}
