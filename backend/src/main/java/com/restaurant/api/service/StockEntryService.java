package com.restaurant.api.service;

import com.restaurant.api.dto.stock.StockEntryRequest;
import com.restaurant.api.dto.stock.StockEntryResponse;
import com.restaurant.api.entity.*;
import com.restaurant.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockEntryService {

    private final StockEntryRepository stockEntryRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * Tạo phiếu nhập kho mới.
     * - Không cho sửa/xóa → mỗi lần nhập là một record độc lập.
     * - Khi nhập kho → tự động tăng stock của Ingredient.
     */
    // ✅ Thay thế toàn bộ nội dung hàm create trong StockEntryService
    // ======================
//  HÀM CREATE ĐÃ FIX FULL
//  Hỗ trợ số âm + Double
// ======================
    public StockEntryResponse create(StockEntryRequest request) {

        // 📝 Validate cơ bản
        if (request.getIngredientId() == null) {
            throw new IllegalArgumentException("Nguyên liệu không được để trống");
        }
        if (request.getQuantity() == null || request.getQuantity() == 0) {
            throw new IllegalArgumentException("Số lượng nhập phải khác 0");
        }

        // 📝 Lấy nguyên liệu
        Ingredient ing = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu"));

        // ⭐ Dùng Double vì stockQuantity là Double
        Double currentStock = ing.getStockQuantity() == null ? 0.0 : ing.getStockQuantity();
        Double change = request.getQuantity().doubleValue();   // Có thể âm hoặc dương

        Double newStock = currentStock + change;

        // ❗ Không cho phép kho < 0
        if (newStock < 0) {
            throw new IllegalArgumentException("Tồn kho không đủ để điều chỉnh âm");
        }

        // ⭐ Update tồn kho
        ing.setStockQuantity(newStock);
        ingredientRepository.save(ing);

        // 📝 Lưu phiếu nhập / phiếu điều chỉnh
        StockEntry entry = StockEntry.builder()
                .ingredient(ing)
                .quantity(request.getQuantity())   // giữ nguyên giá trị âm/dương
                .note(request.getNote())
                .createdBy("admin")               // Rule 54
                .createdAt(LocalDateTime.now())
                .build();

        StockEntry saved = stockEntryRepository.save(entry);

        // ⭐ Trả về DTO
        return StockEntryResponse.builder()
                .id(saved.getId())
                .ingredientId(ing.getId())
                .ingredientName(ing.getName())
                .quantity(saved.getQuantity())
                .note(saved.getNote())
                .createdBy(saved.getCreatedBy())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    /**
     * Lấy danh sách tất cả phiếu nhập kho.
     */
    public List<StockEntryResponse> getAll() {
        return stockEntryRepository.findAll()
                .stream()
                .map(e -> StockEntryResponse.builder()
                        .id(e.getId())
                        .ingredientId(e.getIngredient().getId())
                        .ingredientName(e.getIngredient().getName())
                        .quantity(e.getQuantity())
                        .note(e.getNote())
                        .createdBy(e.getCreatedBy())
                        .createdAt(e.getCreatedAt())
                        .build()
                )
                .toList();
    }
}
