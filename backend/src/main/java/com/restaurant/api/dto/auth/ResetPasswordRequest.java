package com.restaurant.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng cho API reset password
 */
@Getter
@Setter
public class ResetPasswordRequest {

    private String token;           // mã token trong email
    private String newPassword;     // mật khẩu mới
    private String confirmPassword; // xác nhận mật khẩu
}
