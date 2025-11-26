package com.restaurant.api.controller;

import com.restaurant.api.dto.stock.*;
import com.restaurant.api.service.StockEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API quản lý nhập kho nguyên liệu.
 * Không có chức năng sửa/xóa → đảm bảo đúng quy trình kế toán.
 */
@RestController
@RequestMapping("/api/stock-entries")
@RequiredArgsConstructor
public class StockEntryController {

    private final StockEntryService stockEntryService;

    // Lấy danh sách tất cả phiếu nhập
    @GetMapping
    public List<StockEntryResponse> getAll() {
        return stockEntryService.getAll();
    }

    // Tạo mới phiếu nhập
    @PostMapping
    public StockEntryResponse create(@RequestBody StockEntryRequest request) {
        return stockEntryService.create(request);
    }
}
