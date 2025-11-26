package com.restaurant.api.service;

import com.restaurant.api.dto.payment.PaymentCreateRequest;
import com.restaurant.api.dto.payment.PaymentFilterRequest;
import com.restaurant.api.dto.payment.PaymentResponse;
import com.restaurant.api.entity.Invoice;
import com.restaurant.api.entity.Order;
import com.restaurant.api.entity.OrderStatus;
import com.restaurant.api.entity.Payment;
import com.restaurant.api.repository.InvoiceRepository;
import com.restaurant.api.repository.OrderRepository;
import com.restaurant.api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service xử lý nghiệp vụ Payment:
 *  - Tạo payment cho hóa đơn
 *  - Lọc danh sách payment
 *  - Lấy chi tiết payment
 *
 * Option C: Khi tạo Payment → Invoice sẽ chuyển sang PAID,
 *            đồng thời Order liên quan cũng chuyển sang PAID.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    /**
     * Tạo payment cho 1 invoice.
     * FE gọi: POST /api/payments/{invoiceId}
     *
     * Quy trình:
     *  1. Lấy invoice
     *  2. Tạo bản ghi payment
     *  3. Cập nhật invoice → PAID + paidAt = now
     *  4. Cập nhật order → PAID
     */
    @Transactional
    public PaymentResponse createPayment(Long invoiceId, PaymentCreateRequest req) {

        // 1. Lấy invoice
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID = " + invoiceId));

        // 2. Tạo payment
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(req.getAmount());
        payment.setMethod(req.getMethod());
        payment.setStatus(Optional.ofNullable(req.getStatus()).orElse("SUCCESS"));
        payment.setTransactionCode(req.getTransactionCode());
        payment.setCreatedAt(LocalDateTime.now()); // thời điểm thanh toán

        paymentRepository.save(payment);

        // 3. Cập nhật invoice
        invoice.setStatus("PAID");
        invoice.setPaidAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        // 4. Cập nhật order
        Order order = orderRepository.findById(invoice.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order của invoice!"));

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // 5. Trả về DTO
        return PaymentResponse.builder()
                .id(payment.getId())
                .invoiceId(invoice.getId())
                .invoiceCode(invoice.getCode())
                .orderId(invoice.getOrderId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionCode(payment.getTransactionCode())
                .createdAt(payment.getCreatedAt())
                .paidAt(invoice.getPaidAt())
                .build();
    }

    /**
     * Lấy chi tiết 1 payment.
     */
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy payment ID = " + id));

        Invoice invoice = payment.getInvoice();

        return PaymentResponse.builder()
                .id(payment.getId())
                .invoiceId(invoice.getId())
                .invoiceCode(invoice.getCode())
                .orderId(invoice.getOrderId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionCode(payment.getTransactionCode())
                .createdAt(payment.getCreatedAt())
                .paidAt(invoice.getPaidAt())
                .build();
    }

    /**
     * Lọc danh sách payment (có phân trang)
     * Rule 20: Không truyền NULL xuống DB.
     */
    public Page<PaymentResponse> getPayments(PaymentFilterRequest f) {

        // Nếu FE không truyền fromDate → dùng MIN_DATE
        LocalDateTime from = (f.getFromDate() != null)
                ? f.getFromDate().atStartOfDay()
                : LocalDateTime.of(1970, 1, 1, 0, 0);

        // Nếu FE không truyền toDate → dùng MAX_DATE
        LocalDateTime to = (f.getToDate() != null)
                ? f.getToDate().atTime(23, 59, 59)
                : LocalDateTime.of(2999, 12, 31, 23, 59, 59);

        int page = Optional.ofNullable(f.getPage()).orElse(0);
        int size = Optional.ofNullable(f.getSize()).orElse(20);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Payment> payments = paymentRepository.searchPayments(
                from,
                to,
                f.getMethod(),
                f.getStatus(),
                pageable
        );

        return payments.map(p -> PaymentResponse.builder()
                .id(p.getId())
                .invoiceId(p.getInvoice().getId())
                .invoiceCode(p.getInvoice().getCode())
                .orderId(p.getInvoice().getOrderId())
                .amount(p.getAmount())
                .method(p.getMethod())
                .status(p.getStatus())
                .transactionCode(p.getTransactionCode())
                .createdAt(p.getCreatedAt())
                .paidAt(p.getInvoice().getPaidAt())
                .build()
        );
    }
}
