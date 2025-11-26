package com.restaurant.api.dto.invoice;

import lombok.*;

import java.util.List;
import com.restaurant.api.dto.payment.PaymentResponse; // Thông tin payment gắn với hóa đơn

/**
 * DTO InvoiceResponse – đại diện dữ liệu hóa đơn trả về cho FE.
 * Format ngày dạng dd/MM/yyyy HH:mm theo Rule 15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private Long id;                // ID hóa đơn
    private String code;            // Mã hóa đơn (INVXXXX)
    private Long orderId;           // ID đơn hàng gốc
    private Double totalAmount;     // Tổng tiền
    private String status;          // Trạng thái: PAID / UNPAID

    private String createdAt;       // Ngày tạo (string format)
    private String createdBy;       // Người tạo hóa đơn
    private String paidAt;          // Ngày thanh toán (string format) – có thể null

    private List<InvoiceItemResponse> items;  // Danh sách món trong hóa đơn
    private PaymentResponse payment;           // Thông tin thanh toán (nếu hóa đơn đã được thanh toán)
}
