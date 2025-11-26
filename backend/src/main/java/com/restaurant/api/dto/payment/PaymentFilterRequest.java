package com.restaurant.api.dto.payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO nhận dữ liệu filter từ FE khi tìm kiếm danh sách payment.
 */
@Getter
@Setter
public class PaymentFilterRequest {

    // Ngày bắt đầu filter (theo createdAt)
    private LocalDate fromDate;

    // Ngày kết thúc filter (theo createdAt)
    private LocalDate toDate;

    // Phương thức thanh toán (CASH, MOMO, BANK,...)
    private String method;

    // Trạng thái thanh toán (SUCCESS, FAILED, PENDING)
    private String status;

    // Phân trang
    private Integer page; // số trang (bắt đầu từ 0)
    private Integer size; // số phần tử mỗi trang
}
