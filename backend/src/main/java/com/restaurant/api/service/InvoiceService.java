package com.restaurant.api.service;

import com.restaurant.api.dto.invoice.*;
import com.restaurant.api.entity.Invoice;
import com.restaurant.api.entity.InvoiceItem;
import com.restaurant.api.entity.Order;
import com.restaurant.api.entity.OrderItem;
import com.restaurant.api.repository.InvoiceItemRepository;
import com.restaurant.api.repository.InvoiceRepository;
import com.restaurant.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.restaurant.api.entity.Payment;
import com.restaurant.api.repository.PaymentRepository;
import com.restaurant.api.dto.payment.PaymentResponse;

/**
 * Service Invoice – xử lý:
 * - Tạo hóa đơn từ đơn hàng
 * - Lấy danh sách hóa đơn
 * - Lọc hóa đơn
 * - Xem chi tiết hóa đơn
 * - Chuyển đổi entity → DTO
 */
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository; // Repository để lấy payment theo invoice

    // Format ngày cho FE: dd/MM/yyyy HH:mm
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * =============================
     * 1) Tạo HÓA ĐƠN từ ORDER
     * =============================
     */
    public InvoiceCreateResponse createInvoiceFromOrder(Long orderId) {

        // 1. Lấy order từ DB
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID = " + orderId));

        // 2. Sinh mã hóa đơn dạng INV000001
        String newInvoiceCode = generateInvoiceCode();

        // 3. Tính tổng tiền dựa vào order items
        double total = order.getItems().stream()
                .mapToDouble(item -> item.getPrice().doubleValue() * item.getQuantity())
                .sum();

        // 4. Tạo entity Invoice
        Invoice invoice = Invoice.builder()
                .orderId(order.getId())
                .code(newInvoiceCode)
                .totalAmount(total)
                .status("UNPAID")              // Tạm thời PAID (sau này tích hợp Payment Module)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")          // Rule 54 – cách A
                .paidAt(null)
                .build();

        // Lưu invoice
        invoice = invoiceRepository.save(invoice);

        // 5. Snapshot OrderItem → InvoiceItem
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (OrderItem oi : order.getItems()) {

            InvoiceItem ii = InvoiceItem.builder()
                    .invoice(invoice)
                    .name(oi.getDish().getName())     // snapshot tên món
                    .price(oi.getPrice().doubleValue())             // snapshot giá
                    .quantity(oi.getQuantity())
                    .build();

            invoiceItems.add(ii);
        }

        // Lưu toàn bộ invoice_item
        invoiceItemRepository.saveAll(invoiceItems);

        // 6. Trả về DTO
        return InvoiceCreateResponse.builder()
                .invoiceId(invoice.getId())
                .code(invoice.getCode())
                .build();
    }

    /**
     * Hàm sinh mã hóa đơn tự động: INV000001
     */
    private String generateInvoiceCode() {
        long count = invoiceRepository.count() + 1;
        return "INV" + String.format("%06d", count);
    }


    /**
     * =============================
     * 2) Lấy DANH SÁCH hóa đơn
     * =============================
     */
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Invoice::getCreatedAt).reversed())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    /**
     * =============================
     * 3) LỌC + TÌM KIẾM hóa đơn
     * =============================
     */
    public List<InvoiceResponse> filterInvoices(InvoiceFilterRequest req) {

        // Nếu không gửi fromDate → dùng ngày rất nhỏ
        LocalDateTime from = req.getFromDate() == null
                ? LocalDateTime.of(1970, 1, 1, 0, 0)
                : req.getFromDate().atStartOfDay();

        // Nếu không gửi toDate → dùng ngày rất lớn
        LocalDateTime to = req.getToDate() == null
                ? LocalDateTime.of(2999, 12, 31, 23, 59)
                : req.getToDate().atTime(LocalTime.MAX);

        // Keyword rỗng thì set về ""
        String keyword = (req.getKeyword() == null || req.getKeyword().isBlank())
                ? ""
                : req.getKeyword();

        List<Invoice> result = invoiceRepository.filterInvoices(
                from,
                to,
                keyword
        );

        return result.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    /**
     * =============================
     * 4) Lấy CHI TIẾT hóa đơn
     * =============================
     */
    public InvoiceResponse getInvoiceDetail(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID = " + id));

        return toResponse(invoice);
    }

    /**
     * Chuyển từ Entity Invoice sang DTO InvoiceResponse cho FE.
     * - Lấy danh sách món (InvoiceItemResponse)
     * - Lấy thông tin payment (PaymentResponse) nếu hóa đơn đã được thanh toán
     * - Format ngày theo Rule 15: dd/MM/yyyy HH:mm
     */
    private InvoiceResponse toResponse(Invoice invoice) {

        // 1. Lấy danh sách món trong hóa đơn
        List<InvoiceItemResponse> items =
                invoiceItemRepository.findByInvoiceId(invoice.getId())
                        .stream()
                        .map(ii -> InvoiceItemResponse.builder()
                                .id(ii.getId())
                                .name(ii.getName())
                                .price(ii.getPrice())
                                .quantity(ii.getQuantity())
                                .build())
                        .collect(Collectors.toList());

        // 2. Lấy payment gắn với hóa đơn (nếu có)
        PaymentResponse paymentDto = null;

        Optional<Payment> paymentOpt = paymentRepository.findByInvoice_Id(invoice.getId());
        if (paymentOpt.isPresent()) {
            Payment p = paymentOpt.get();

            // Map Payment -> PaymentResponse (DTO dùng cho FE)
            paymentDto = PaymentResponse.builder()
                    .id(p.getId())
                    .invoiceId(invoice.getId())
                    .invoiceCode(invoice.getCode())
                    .orderId(invoice.getOrderId())
                    .amount(p.getAmount())
                    .method(p.getMethod())
                    .status(p.getStatus())
                    .transactionCode(p.getTransactionCode())
                    .createdAt(p.getCreatedAt())
                    .paidAt(invoice.getPaidAt()) // thời điểm hóa đơn được đánh dấu đã thanh toán
                    .build();
        }

        // 3. Build InvoiceResponse trả về cho FE
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .code(invoice.getCode())
                .orderId(invoice.getOrderId())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .createdAt(format(invoice.getCreatedAt())) // dùng helper format
                .createdBy(invoice.getCreatedBy())
                .paidAt(invoice.getPaidAt() != null ? format(invoice.getPaidAt()) : null)
                .items(items)
                .payment(paymentDto) // ⭐ gắn thông tin payment vào response
                .build();
    }

    // Helper format ngày
    private String format(LocalDateTime time) {
        if (time == null) return null;
        return time.format(DATE_FORMAT);
    }

    /**
     * Thanh toán hóa đơn
     * - Chỉ áp dụng cho hóa đơn chưa thanh toán
     * - Cập nhật trạng thái sang "PAID"
     * - Ghi lại thời điểm thanh toán (paidAt)
     */
    public InvoiceResponse payInvoice(Long id) {

        // Lấy hóa đơn theo ID
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID = " + id));

        // Nếu đã thanh toán rồi thì không cho thanh toán lại
        if ("PAID".equals(invoice.getStatus())) {
            throw new RuntimeException("Hóa đơn đã được thanh toán trước đó");
        }

        // Cập nhật trạng thái sang PAID
        invoice.setStatus("PAID");

        // Ghi lại thời gian thanh toán
        invoice.setPaidAt(LocalDateTime.now());

        // Lưu vào DB
        invoiceRepository.save(invoice);

        // Trả dữ liệu response chuẩn
        return toResponse(invoice);
    }

}
