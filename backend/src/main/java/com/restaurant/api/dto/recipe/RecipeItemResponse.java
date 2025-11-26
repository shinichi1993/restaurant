package com.restaurant.api.dto.recipe;

import lombok.*;

/**
 * DTO trả về cho FE khi xem danh sách định lượng
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeItemResponse {

    private Long id;                // ID dòng recipe_item
    private Long ingredientId;      // ID nguyên liệu
    private String ingredientName;  // Tên nguyên liệu (hiển thị trên UI)
    private Double quantityNeeded;  // Số gram/ml cần
}
