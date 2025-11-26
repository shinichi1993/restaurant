package com.restaurant.api.controller;

import com.restaurant.api.dto.order.OrderCreateRequest;
import com.restaurant.api.dto.order.OrderResponse;
import com.restaurant.api.dto.order.OrderUpdateRequest;
import com.restaurant.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restaurant.api.dto.invoice.InvoiceResponse;

import java.util.List;

/**
 * REST Controller cho chức năng quản lý đơn hàng (Order).
 */
@RestController
@RequestMapping("/api/orders") // FE sẽ gọi qua axiosClient với path "/orders"
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Lấy danh sách tất cả đơn hàng.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        List<OrderResponse> data = orderService.getAll();
        return ResponseEntity.ok(data);
    }

    /**
     * Lấy chi tiết 1 đơn hàng theo id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    /**
     * Tạo mới 1 đơn hàng.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    /**
     * Cập nhật 1 đơn hàng.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                @RequestBody OrderUpdateRequest request) {
        return ResponseEntity.ok(orderService.update(id, request));
    }

    /**
     * Xóa 1 đơn hàng.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * API thanh toán đơn hàng và tự động tạo hóa đơn
     * - Đường dẫn: POST /orders/{id}/pay
     * - Input: ID đơn hàng
     * - Output: InvoiceResponse (chi tiết hóa đơn vừa tạo)
     */
    @PostMapping("/{id}/pay")
    public InvoiceResponse payOrder(@PathVariable Long id) {
        // Gọi service xử lý thanh toán + tạo hóa đơn
        return orderService.payOrderAndCreateInvoice(id);
    }

}
