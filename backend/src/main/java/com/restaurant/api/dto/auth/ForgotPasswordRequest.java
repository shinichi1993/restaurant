package com.restaurant.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng cho API quên mật khẩu
 */
@Getter
@Setter
public class ForgotPasswordRequest {
    private String email; // email user nhập
}
