package com.restaurant.api.dto.ingredient;

import lombok.*;

/**
 * DTO trả ra FE – format datetime theo chuẩn dd/MM/yyyy HH:mm.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponse {

    private Long id;
    private String name;
    private String unit;
    private Double stockQuantity;
    private Double minStock;
    private String createdAt;   // Định dạng string dd/MM/yyyy HH:mm
    private String createdBy;
}
