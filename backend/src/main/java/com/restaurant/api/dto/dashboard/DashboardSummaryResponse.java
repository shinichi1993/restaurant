package com.restaurant.api.dto.dashboard;

import lombok.*;
import java.math.BigDecimal;

/**
 * DTO trả về dữ liệu tổng quan Dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryResponse {

    private long totalOrders;         // Tổng số hóa đơn
    private BigDecimal totalRevenue;  // Tổng doanh thu

    private long paidOrders;          // Số hóa đơn đã thanh toán
    private long pendingOrders;       // Số hóa đơn chưa thanh toán

    private BigDecimal todayRevenue;  // Doanh thu hôm nay
    private long todayOrders;         // Số đơn hôm nay
}
