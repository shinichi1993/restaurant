package com.restaurant.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO nhận refreshToken từ client
 */
@Getter
@Setter
public class RefreshTokenRequest {
    private String refreshToken;
}
