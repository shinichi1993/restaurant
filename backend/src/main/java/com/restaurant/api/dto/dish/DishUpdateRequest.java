package com.restaurant.api.dto.dish;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO cập nhật món ăn
 */
@Getter
@Setter
public class DishUpdateRequest {

    /** Tên món ăn */
    private String name;

    /** Mô tả món ăn */
    private String description;

    /** Giá món ăn */
    private BigDecimal price;

    /** Link ảnh */
    private String imageUrl;

    /** Category mới (nếu đổi) */
    private Long categoryId;
}
