package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Order – Đại diện cho 1 đơn hàng trong quán.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders") // Dùng tên bảng "orders" tránh trùng từ khóa "order"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính

    @Column(nullable = false, unique = true)
    private String code; // Mã đơn (ORD-...)

    private String customerName;  // Tên khách (có thể null)

    private String customerPhone; // SĐT khách (có thể null)

    private String tableNumber;   // Số bàn (có thể null nếu mang về)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;   // Trạng thái đơn (PENDING / COOKING / DONE / CANCELLED)

    @Column(nullable = false)
    private BigDecimal totalAmount; // Tổng tiền của đơn

    @Column(length = 1000)
    private String note; // Ghi chú cho bếp/phục vụ

    @Column(nullable = false)
    private String createdBy; // Người tạo đơn (tạm thời là "admin" – Rule 54)

    @Column(nullable = false)
    private LocalDateTime createdAt; // Thời điểm tạo

    @Column(nullable = false)
    private LocalDateTime updatedAt; // Thời điểm cập nhật gần nhất

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>(); // Danh sách món trong đơn

    /**
     * Hàm tiện ích để gắn OrderItem vào Order và set quan hệ 2 chiều.
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Hàm tiện ích để bỏ OrderItem khỏi Order và clear quan hệ 2 chiều.
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
