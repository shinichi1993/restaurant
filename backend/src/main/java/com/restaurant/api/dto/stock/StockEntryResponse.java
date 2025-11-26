package com.restaurant.api.dto.stock;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Response trả về sau khi tạo hoặc lấy danh sách phiếu nhập kho.
 */
@Getter
@Setter
@Builder
public class StockEntryResponse {

    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private Integer quantity;
    private String note;
    private String createdBy;
    private LocalDateTime createdAt;
}
