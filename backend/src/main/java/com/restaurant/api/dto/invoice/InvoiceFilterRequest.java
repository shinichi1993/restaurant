package com.restaurant.api.dto.invoice;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO lọc hóa đơn theo yêu cầu FE.
 * - fromDate, toDate: lọc theo ngày
 * - keyword: tìm theo mã hóa đơn hoặc created_by
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceFilterRequest {

    private LocalDate fromDate;   // Ngày bắt đầu
    private LocalDate toDate;     // Ngày kết thúc
    private String keyword;       // Từ khóa tìm kiếm
}
