package com.restaurant.api.dto;

import lombok.*;

/**
 * Request DTO dùng để cập nhật cấu hình
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingRequest {

    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;
    private String restaurantEmail;
    private String logoUrl;

    private Double vatPercent;
    private Double servicePercent;
    private String openingTime;
    private String closingTime;
    private Integer tableCount;

    private String invoiceHeader;
    private String invoiceFooter;
}
