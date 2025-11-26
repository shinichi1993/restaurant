package com.restaurant.api.dto.dashboard;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO doanh thu theo từng ngày.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyRevenueResponse {

    private LocalDate date;      // Ngày thống kê
    private BigDecimal revenue;  // Doanh thu
    private long orderCount;     // Số đơn
}
