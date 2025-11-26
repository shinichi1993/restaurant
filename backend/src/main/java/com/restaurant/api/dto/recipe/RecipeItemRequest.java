package com.restaurant.api.dto.recipe;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng khi tạo / cập nhật định lượng nguyên liệu cho món
 */
@Getter
@Setter
public class RecipeItemRequest {

    /**
     * ID nguyên liệu sẽ gán cho món
     */
    private Long ingredientId;

    /**
     * Số lượng cần cho 1 phần món (gram/ml)
     */
    private Double quantityNeeded;
}
