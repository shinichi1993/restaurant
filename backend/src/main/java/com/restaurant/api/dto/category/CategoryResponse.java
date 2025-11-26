package com.restaurant.api.dto.category;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO trả về cho FE khi lấy danh sách category
 */
@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;

    // Ngày tạo (hiển thị FE)
    private LocalDateTime createdAt;
}
