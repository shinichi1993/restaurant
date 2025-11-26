package com.restaurant.api.dto.auth;

import lombok.*;

/**
 * DTO trả về sau khi login/register
 * bao gồm AccessToken + RefreshToken
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;    // JWT access token
    private String refreshToken;   // Refresh token
    private String tokenType;      // "Bearer"
    private Long expiresIn;        // Số giây sống của accessToken

    // Thông tin cơ bản của user
    private Long userId;
    private String fullName;
    private String email;
}
