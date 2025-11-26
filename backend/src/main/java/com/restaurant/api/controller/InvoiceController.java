package com.restaurant.api.controller;

import com.restaurant.api.dto.invoice.InvoiceCreateResponse;
import com.restaurant.api.dto.invoice.InvoiceFilterRequest;
import com.restaurant.api.dto.invoice.InvoiceResponse;
import com.restaurant.api.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller Invoice – xử lý các API của Module 07:
 * - Tạo hóa đơn từ Order
 * - Lọc hóa đơn
 * - Danh sách hóa đơn
 * - Xem chi tiết hóa đơn
 * - API tải PDF (dummy)
 *
 * Tất cả trả về DTO theo đúng format FE cần.
 */
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    /**
     * API 1: Tạo hóa đơn từ Order
     * ------------------------------------------
     * Khi tạo hóa đơn:
     *  - Snapshot dữ liệu OrderItem → InvoiceItem
     *  - Sinh mã hóa đơn INVxxxx
     *  - Lưu hóa đơn và chi tiết
     */
    @PostMapping("/create-from-order/{orderId}")
    public InvoiceCreateResponse createInvoice(@PathVariable Long orderId) {
        return invoiceService.createInvoiceFromOrder(orderId);
    }

    /**
     * API 2: Danh sách tất cả hóa đơn
     */
    @GetMapping
    public List<InvoiceResponse> getAll() {
        return invoiceService.getAllInvoices();
    }

    /**
     * API 3: Lọc hóa đơn theo:
     *  - fromDate: ngày bắt đầu
     *  - toDate: ngày kết thúc
     *  - keyword: tìm theo mã hoặc createdBy
     */
    @PostMapping("/filter")
    public List<InvoiceResponse> filter(@RequestBody InvoiceFilterRequest request) {
        return invoiceService.filterInvoices(request);
    }

    /**
     * API 4: Chi tiết hóa đơn theo ID
     */
    @GetMapping("/{id}")
    public InvoiceResponse getDetail(@PathVariable Long id) {
        return invoiceService.getInvoiceDetail(id);
    }

    /**
     * API 5: Xuất PDF hóa đơn
     * ------------------------------------------
     * Module 07 chưa làm PDF thật (chỉ làm dummy).
     * Module 09 mới làm PDF hoàn chỉnh.
     * API này trả về byte[] giả để FE test nút "In hóa đơn".
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {

        // PDF fake 1 trang: "Hóa đơn ID = {id}"
        String content = "PDF HÓA ĐƠN (DUMMY) – Invoice ID: " + id;
        byte[] pdfBytes = content.getBytes(); // Module 09 sẽ thay bằng PDF thật

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("invoice_" + id + ".pdf").build()
        );

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * API thanh toán hóa đơn
     * - Đường dẫn: POST /invoices/{id}/pay
     * - Khi gọi sẽ cập nhật trạng thái hóa đơn sang PAID
     */
    @PostMapping("/{id}/pay")
    public InvoiceResponse pay(@PathVariable Long id) {
        return invoiceService.payInvoice(id); // gọi service xử lý
    }
}
