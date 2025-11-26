package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity OrderItem – Chi tiết từng món trong đơn hàng.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Đơn hàng cha

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish; // Món ăn (liên kết với bảng dish đã có ở Module 2)

    @Column(nullable = false)
    private Integer quantity; // Số lượng món

    @Column(nullable = false)
    private BigDecimal price; // Đơn giá 1 phần tại thời điểm đặt

    @Column(nullable = false)
    private BigDecimal amount; // Thành tiền = quantity * price
}
