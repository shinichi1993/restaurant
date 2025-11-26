package com.restaurant.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng cho API đổi mật khẩu
 * MODULE 01 – Authentication
 */
@Getter
@Setter
public class ChangePasswordRequest {

    private String oldPassword;      // mật khẩu cũ
    private String newPassword;      // mật khẩu mới
    private String confirmPassword;  // xác nhận mật khẩu
}
