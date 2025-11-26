package com.restaurant.api.dto.invoice;

import lombok.*;

/**
 * DTO trả về sau khi tạo hóa đơn từ đơn hàng.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceCreateResponse {

    private Long invoiceId;   // ID hóa đơn vừa tạo
    private String code;      // Mã hóa đơn
}
