package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

/**
 * Entity User tương ứng bảng 'users'
 * Module 01 - Authentication
 * Không có phân quyền.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email đăng nhập duy nhất
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    // Mật khẩu được mã hóa bằng BCrypt
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // Họ tên user
    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    // Số điện thoại (tùy chọn)
    @Column(length = 20)
    private String phone;

    // Link ảnh đại diện (tùy chọn)
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    // Tài khoản còn hoạt động hay bị khóa
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Thời điểm đăng nhập cuối
    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    // Thời điểm tạo
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // Thời điểm cập nhật
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Callback để set createdAt/updatedAt tự động
    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
