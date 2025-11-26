package com.restaurant.api.dto.user;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO tạo user mới (User Management)
 */
@Getter
@Setter
public class CreateUserRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password; // FE sẽ gửi password mặc định
}
