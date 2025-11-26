package com.restaurant.api.service;

import com.restaurant.api.dto.order.*;
import com.restaurant.api.entity.*;
import com.restaurant.api.repository.DishRepository;
import com.restaurant.api.repository.IngredientRepository;
import com.restaurant.api.repository.OrderRepository;
import com.restaurant.api.repository.RecipeItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.restaurant.api.dto.invoice.InvoiceCreateResponse;
import com.restaurant.api.dto.invoice.InvoiceResponse;
import com.restaurant.api.entity.Order;

/**
 * Service xử lý logic nghiệp vụ cho đơn hàng.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository; // Đã có từ Module 2
    // ✅ Thêm 2 repository mới để làm tiêu kho
    private final IngredientRepository ingredientRepository;
    private final RecipeItemRepository recipeItemRepository;
    private final InvoiceService invoiceService;

    /**
     * Lấy danh sách tất cả đơn hàng.
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lấy chi tiết 1 đơn hàng theo id.
     */
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng với id = " + id));
        return toResponse(order);
    }

    /**
     * Tạo mới đơn hàng.
     * Bổ sung:
     * - Lấy định lượng (RecipeItem) theo từng món
     * - Tính tổng nguyên liệu cần dùng
     * - Kiểm tra tồn kho
     * - Nếu đủ → trừ stockQuantity trong bảng ingredient
     * - Nếu thiếu → throw exception, KHÔNG tạo đơn hàng
     */
    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        // Khởi tạo Order (chưa lưu DB vội)
        Order order = new Order();
        order.setCode(generateOrderCode()); // Mã đơn tự sinh
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setTableNumber(request.getTableNumber());
        order.setNote(request.getNote());
        order.setStatus(OrderStatus.PENDING); // Mặc định đơn mới là PENDING
        order.setCreatedBy("admin"); // Rule 54: tạm thời ghi cứng "admin"
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Xử lý danh sách món trong đơn (OrderItem)
        if (request.getItems() != null) {
            for (OrderItemRequest itemReq : request.getItems()) {
                // Lấy thông tin món từ DB
                Dish dish = dishRepository.findById(itemReq.getDishId())
                        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy món với id = " + itemReq.getDishId()));

                // Giá tại thời điểm hiện tại
                BigDecimal price = dish.getPrice();
                Integer quantity = itemReq.getQuantity();
                BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

                // Tạo OrderItem
                OrderItem orderItem = OrderItem.builder()
                        .dish(dish)
                        .quantity(quantity)
                        .price(price)
                        .amount(amount)
                        .build();

                // Gắn vào Order (quan hệ 2 chiều)
                order.addItem(orderItem);

                // Cộng dồn tổng tiền
                totalAmount = totalAmount.add(amount);
            }
        }

        order.setTotalAmount(totalAmount);

        // ✅ BƯỚC MỚI: TÍNH TOÁN VÀ TIÊU KHO TRƯỚC KHI LƯU ORDER
        // - Dùng dữ liệu từ request.getItems()
        // - Dựa trên RecipeItem (định lượng món)
        Map<Long, BigDecimal> requiredMap = calculateRequiredIngredients(request.getItems());
        consumeStock(requiredMap);

        // Sau khi tiêu kho thành công → lưu Order + OrderItem
        Order saved = orderRepository.save(order);

        return toResponse(saved);
    }

    /**
     * Cập nhật thông tin đơn hàng.
     */
    @Transactional
    public OrderResponse update(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng với id = " + id));

        // Cập nhật thông tin cơ bản
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setTableNumber(request.getTableNumber());
        order.setNote(request.getNote());

        // Cập nhật trạng thái nếu có gửi lên
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        // Nếu FE gửi danh sách món mới -> thay toàn bộ
        if (request.getItems() != null) {
            // Xóa danh sách item cũ (orphanRemoval = true sẽ tự xóa trong DB)
            order.getItems().clear();

            BigDecimal totalAmount = BigDecimal.ZERO;

            for (OrderItemRequest itemReq : request.getItems()) {
                Dish dish = dishRepository.findById(itemReq.getDishId())
                        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy món với id = " + itemReq.getDishId()));

                BigDecimal price = dish.getPrice();
                Integer quantity = itemReq.getQuantity();
                BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

                OrderItem orderItem = OrderItem.builder()
                        .dish(dish)
                        .quantity(quantity)
                        .price(price)
                        .amount(amount)
                        .build();

                order.addItem(orderItem);
                totalAmount = totalAmount.add(amount);
            }

            order.setTotalAmount(totalAmount);
        }

        // Cập nhật thời gian update
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        return toResponse(saved);
    }

    /**
     * Xóa đơn hàng theo id.
     */
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng với id = " + id);
        }
        orderRepository.deleteById(id);
    }

    /**
     * Mapping từ Entity Order sang DTO OrderResponse.
     */
    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .dishId(item.getDish().getId())
                        .dishName(item.getDish().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .amount(item.getAmount())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .code(order.getCode())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .tableNumber(order.getTableNumber())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .note(order.getNote())
                .createdBy(order.getCreatedBy())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(itemResponses)
                .build();
    }

    /**
     * Sinh mã đơn đơn giản dựa trên thời gian.
     * Có thể nâng cấp sau thành format đẹp hơn (ví dụ: ORD-20251124-0001).
     */
    private String generateOrderCode() {
        return "ORD-" + System.currentTimeMillis();
    }

    /**
     * Tính tổng số lượng nguyên liệu cần dùng cho 1 đơn hàng,
     * dựa trên:
     * - Danh sách món trong order (OrderItemRequest)
     * - Định lượng RecipeItem (nguyên liệu / 1 phần món)
     *
     * Kết quả trả về:
     * - Map<ingredientId, quantityNeeded>
     */
    private Map<Long, BigDecimal> calculateRequiredIngredients(List<OrderItemRequest> items) {
        Map<Long, BigDecimal> requiredMap = new HashMap<>();

        if (items == null || items.isEmpty()) {
            return requiredMap;
        }

        for (OrderItemRequest itemReq : items) {
            Long dishId = itemReq.getDishId();
            Integer orderQuantity = itemReq.getQuantity();

            // Lấy danh sách định lượng cho món này
            List<RecipeItem> recipeItems = recipeItemRepository.findByDishId(dishId);

            if (recipeItems == null || recipeItems.isEmpty()) {
                throw new IllegalStateException("Món ăn (id=" + dishId + ") chưa được khai báo định lượng, không thể tạo đơn hàng.");
            }

            for (RecipeItem recipeItem : recipeItems) {
                Ingredient ingredient = recipeItem.getIngredient();
                Long ingredientId = ingredient.getId();

                // Số lượng nguyên liệu cần cho 1 phần món
                BigDecimal perPortion = BigDecimal.valueOf(recipeItem.getQuantityNeeded()); // giả định quantity = BigDecimal
                // Nếu bạn dùng Double ở RecipeItem, có thể đổi sang:
                // BigDecimal perPortion = BigDecimal.valueOf(recipeItem.getQuantity());

                // Tổng cần dùng = định lượng 1 phần * số phần trong order
                BigDecimal totalForThisOrderItem = perPortion.multiply(BigDecimal.valueOf(orderQuantity));

                // Cộng dồn vào map theo ingredientId
                requiredMap.merge(ingredientId, totalForThisOrderItem, BigDecimal::add);
            }
        }

        return requiredMap;
    }

    /**
     * Kiểm tra tồn kho và trừ stockQuantity cho từng nguyên liệu.
     *
     * Quy tắc:
     * - Nếu bất kỳ nguyên liệu nào không đủ tồn kho → throw exception, không trừ gì cả.
     * - Nếu tất cả đủ → trừ và lưu lại.
     */
    private void consumeStock(Map<Long, BigDecimal> requiredMap) {
        if (requiredMap.isEmpty()) {
            // Không có món / không có định lượng → không trừ kho
            return;
        }

        // Lấy toàn bộ nguyên liệu liên quan trong 1 query
        List<Long> ingredientIds = new ArrayList<>(requiredMap.keySet());
        List<Ingredient> ingredients = ingredientRepository.findAllById(ingredientIds);

        // Map tạm để truy cập nhanh theo id
        Map<Long, Ingredient> ingredientMap = ingredients.stream()
                .collect(Collectors.toMap(Ingredient::getId, ing -> ing));

        // 1. KIỂM TRA ĐỦ KHO HAY KHÔNG
        for (Map.Entry<Long, BigDecimal> entry : requiredMap.entrySet()) {
            Long ingredientId = entry.getKey();
            BigDecimal requiredQty = entry.getValue();

            Ingredient ingredient = ingredientMap.get(ingredientId);
            if (ingredient == null) {
                throw new IllegalStateException("Không tìm thấy nguyên liệu với id = " + ingredientId);
            }

            BigDecimal currentStock = BigDecimal.valueOf(ingredient.getStockQuantity());

            // Nếu bạn dùng kiểu khác (Integer/Double) cho stockQuantity, hãy chuyển sang BigDecimal tương ứng.

            if (currentStock.compareTo(requiredQty) < 0) {
                throw new IllegalStateException(
                        "Nguyên liệu '" + ingredient.getName() + "' không đủ tồn kho. Cần " +
                                requiredQty + " " + ingredient.getUnit() +
                                ", nhưng chỉ còn " + currentStock + " " + ingredient.getUnit()
                );
            }
        }

        // 2. NẾU TẤT CẢ ĐỦ → TIẾN HÀNH TRỪ KHO
        for (Map.Entry<Long, BigDecimal> entry : requiredMap.entrySet()) {
            Long ingredientId = entry.getKey();
            BigDecimal requiredQty = entry.getValue();

            Ingredient ingredient = ingredientMap.get(ingredientId);

            BigDecimal currentStock = BigDecimal.valueOf(ingredient.getStockQuantity());

            BigDecimal newStock = currentStock.subtract(requiredQty);

            ingredient.setStockQuantity(newStock.doubleValue());
        }

        // 3. LƯU LẠI CÁC NGUYÊN LIỆU ĐÃ CẬP NHẬT TỒN KHO
        ingredientRepository.saveAll(ingredients);
    }

    /**
     * Thanh toán đơn hàng và tự động tạo hóa đơn
     * - Bước 1: Kiểm tra đơn hàng tồn tại
     * - Bước 2: Kiểm tra trạng thái (tránh thanh toán 2 lần)
     * - Bước 3: Cập nhật trạng thái Order sang PAID
     * - Bước 4: Gọi InvoiceService để tạo hóa đơn từ Order
     * - Bước 5: Trả về chi tiết hóa đơn (InvoiceResponse) để FE hiển thị
     */
    public InvoiceResponse payOrderAndCreateInvoice(Long orderId) {

        // Lấy đơn hàng theo ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID = " + orderId));

        // Nếu đơn hàng đã ở trạng thái PAID thì không cho thanh toán lại
        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Đơn hàng đã được thanh toán trước đó");
        }

        // Cập nhật trạng thái đơn hàng sang PAID
        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order); // lưu thay đổi

        // Gọi InvoiceService để tạo hóa đơn từ Order
        InvoiceCreateResponse createResp = invoiceService.createInvoiceFromOrder(orderId);

        // Lấy chi tiết hóa đơn vừa tạo để trả về cho FE
        return invoiceService.getInvoiceDetail(createResp.getInvoiceId());
    }


}
