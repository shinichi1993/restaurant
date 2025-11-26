package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity Setting – Lưu trữ toàn bộ cấu hình hệ thống
 * Mỗi hệ thống chỉ có 1 bản ghi duy nhất.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "setting")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Thông tin quán ăn ---
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;
    private String restaurantEmail;
    private String logoUrl;

    // --- Cấu hình chung ---
    private Double vatPercent;
    private Double servicePercent;
    private String openingTime;
    private String closingTime;
    private Integer tableCount;

    // --- Cấu hình in hóa đơn ---
    private String invoiceHeader;
    private String invoiceFooter;

    private LocalDateTime updatedAt;
}
