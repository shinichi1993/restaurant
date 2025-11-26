package com.restaurant.api.controller;

import com.restaurant.api.dto.payment.PaymentCreateRequest;
import com.restaurant.api.dto.payment.PaymentFilterRequest;
import com.restaurant.api.dto.payment.PaymentResponse;
import com.restaurant.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * REST API cho nghiệp vụ Payment.
 * URL đều bắt đầu bằng /api theo chuẩn toàn dự án.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * API lấy danh sách payment theo filter + phân trang.
     * Method: GET (sử dụng query param cho đơn giản)
     *
     * Ví dụ:
     *   /api/payments?fromDate=2025-11-01&toDate=2025-11-30&method=CASH&status=SUCCESS&page=0&size=20
     */
    @GetMapping
    public Page<PaymentResponse> getPayments(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        // Map query param -> DTO filter
        PaymentFilterRequest filter = new PaymentFilterRequest();

        // Parse fromDate/toDate dạng yyyy-MM-dd nếu FE truyền
        // Nếu FE không truyền, để null để Rule 20 xử lý trong service
        if (fromDate != null && !fromDate.isBlank()) {
            filter.setFromDate(java.time.LocalDate.parse(fromDate));
        }
        if (toDate != null && !toDate.isBlank()) {
            filter.setToDate(java.time.LocalDate.parse(toDate));
        }

        filter.setMethod(method);
        filter.setStatus(status);
        filter.setPage(page);
        filter.setSize(size);

        return paymentService.getPayments(filter);
    }

    /**
     * API lấy chi tiết 1 payment theo ID.
     * Method: GET /api/payments/{id}
     */
    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    /**
     * API tạo payment cho một invoice.
     * Tạm thời dùng phương thức POST:
     *   /api/payments/{invoiceId}
     *
     * Body: PaymentCreateRequest (amount, method, status, transactionCode)
     */
    @PostMapping("/{invoiceId}")
    public PaymentResponse createPayment(
            @PathVariable Long invoiceId,
            @RequestBody PaymentCreateRequest request
    ) {
        return paymentService.createPayment(invoiceId, request);
    }
}
