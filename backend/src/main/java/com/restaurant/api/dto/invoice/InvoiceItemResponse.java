package com.restaurant.api.dto.invoice;

import lombok.*;

/**
 * DTO InvoiceItemResponse – trả các món đã snapshot trong hóa đơn.
 * Dùng cho màn chi tiết hóa đơn.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemResponse {
    private Long id;        // ID invoice_item
    private String name;    // Tên món snapshot
    private Double price;   // Giá snapshot
    private Integer quantity; // Số lượng
}
