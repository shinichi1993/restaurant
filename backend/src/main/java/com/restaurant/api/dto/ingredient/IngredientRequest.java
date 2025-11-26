package com.restaurant.api.dto.ingredient;

import lombok.*;

/**
 * DTO cho request tạo/sửa nguyên liệu.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRequest {

    private String name;        // Tên nguyên liệu
    private String unit;        // Đơn vị tính
    private Double stockQuantity; // Tồn kho hiện tại
    private Double minStock;      // Ngưỡng cảnh báo
}
