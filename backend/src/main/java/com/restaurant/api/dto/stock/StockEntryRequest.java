package com.restaurant.api.dto.stock;

import lombok.Getter;
import lombok.Setter;

/**
 * Request khi tạo phiếu nhập kho.
 */
@Getter
@Setter
public class StockEntryRequest {

    private Long ingredientId;     // ID nguyên liệu
    private Integer quantity;      // Số lượng nhập
    private String note;           // Ghi chú
}
