package com.restaurant.api.controller;

import com.restaurant.api.dto.auth.*;
import com.restaurant.api.entity.User;
import com.restaurant.api.service.AuthService;
import com.restaurant.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import com.restaurant.api.service.PasswordResetService;

/**
 * Controller xử lý Register + Login
 * MODULE 01 - Authentication
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;

    /**
     * API Đăng ký
     * POST /api/auth/register
     * Body: RegisterRequest
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * API Đăng nhập
     * POST /api/auth/login
     * Body: LoginRequest
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * API lấy thông tin user hiện tại
     * GET /api/auth/me
     * Yêu cầu: access token hợp lệ
     */
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Trả thông tin cơ bản theo SRS
        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "phone", user.getPhone(),
                        "avatarUrl", user.getAvatarUrl(),
                        "lastLoginAt", user.getLastLoginAt(),
                        "createdAt", user.getCreatedAt()
                )
        );
    }

    /**
     * API Refresh Token
     * POST /api/auth/refresh
     * Body: { "refreshToken": "..." }
     * Trả về accessToken mới
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Quên mật khẩu – Gửi email chứa token reset
     * POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.forgotPassword(request);
        return ResponseEntity.ok(
                Map.of("message", "If this email is registered, a reset link has been sent.")
        );
    }

    /**
     * Reset mật khẩu bằng token
     * POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok(
                Map.of("message", "Password has been reset successfully.")
        );
    }

    /**
     * Đổi mật khẩu khi user đã đăng nhập
     * POST /api/auth/change-password
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {

        // Lấy user hiện tại từ SecurityContext (JWT Filter)
        User user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Xử lý đổi mật khẩu
        userService.changePassword(
                user,
                request.getOldPassword(),
                request.getNewPassword(),
                request.getConfirmPassword(),
                passwordEncoder
        );

        return ResponseEntity.ok(
                Map.of("message", "Password changed successfully.")
        );
    }


}
