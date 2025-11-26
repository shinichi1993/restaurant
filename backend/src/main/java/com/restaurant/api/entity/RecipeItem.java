package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity RecipeItem
 * - Lưu định lượng nguyên liệu cho từng món ăn
 * - Mỗi bản ghi = 1 nguyên liệu cho 1 món + số gram/ml cần dùng cho 1 phần
 */
@Entity
@Table(name = "recipe_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Món ăn tương ứng
     * ManyToOne vì: 1 món có nhiều dòng recipe_item
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    /**
     * Nguyên liệu tương ứng
     * ManyToOne vì: 1 nguyên liệu có thể được dùng cho nhiều món
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    /**
     * Số lượng nguyên liệu cần cho 1 phần món
     * Đơn vị: gram/ml (thống nhất theo SRS)
     */
    @Column(name = "quantity_needed", nullable = false)
    private Double quantityNeeded;
}
