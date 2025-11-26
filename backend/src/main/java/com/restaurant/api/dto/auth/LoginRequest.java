package com.restaurant.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO cho API đăng nhập (login)
 */
@Getter
@Setter
public class LoginRequest {

    private String email;     // Email đăng nhập
    private String password;  // Mật khẩu
}
