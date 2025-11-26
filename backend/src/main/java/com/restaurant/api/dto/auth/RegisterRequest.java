package com.restaurant.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO cho API đăng ký (register)
 * Module 01 – Authentication
 */
@Getter
@Setter
public class RegisterRequest {

    private String fullName;      // Họ tên người dùng
    private String email;         // Email đăng nhập
    private String password;      // Mật khẩu
    private String confirmPassword; // Xác nhận mật khẩu
    private String phone;         // Số điện thoại (tùy chọn)
}
