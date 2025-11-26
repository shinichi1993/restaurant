package com.restaurant.api.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO trả về thông tin 1 đơn hàng đầy đủ.
 * FE sẽ format ngày giờ sang "dd/MM/yyyy HH:mm".
 */
@Data
@Builder
public class OrderResponse {

    private Long id;

    private String code; // Mã đơn

    private String customerName;

    private String customerPhone;

    private String tableNumber;

    private String status; // Chuỗi PENDING / COOKING / DONE / CANCELLED

    private BigDecimal totalAmount;

    private String note;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrderItemResponse> items; // Danh sách món trong đơn
}
