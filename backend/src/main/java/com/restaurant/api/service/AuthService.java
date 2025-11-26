package com.restaurant.api.service;

import com.restaurant.api.dto.auth.AuthResponse;
import com.restaurant.api.dto.auth.LoginRequest;
import com.restaurant.api.dto.auth.RegisterRequest;
import com.restaurant.api.entity.RefreshToken;
import com.restaurant.api.entity.User;
import com.restaurant.api.repository.RefreshTokenRepository;
import com.restaurant.api.repository.UserRepository;
import com.restaurant.api.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * Xử lý logic đăng ký & đăng nhập
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    /** ===========================
     *  REGISTER
     *  =========================== */
    public AuthResponse register(RegisterRequest request) {

        // 1) Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại.");
        }

        // 2) Kiểm tra password == confirmPassword
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        // 3) Mã hóa mật khẩu bằng BCrypt
        String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        // 4) Tạo User
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(hashed)
                .phone(request.getPhone())
                .isActive(true)
                .build();

        userRepository.save(user);

        // 5) Tạo AccessToken
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());

        // 6) Tạo RefreshToken
        String refreshTokenString = java.util.UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenString)
                .expiredAt(OffsetDateTime.now().plusDays(30)) // refresh token sống 30 ngày
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        // 7) Trả về AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }

    /** ===========================
     *  LOGIN
     *  =========================== */
    public AuthResponse login(LoginRequest request) {

        // 1) Kiểm tra email có tồn tại
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng."));

        // 2) Kiểm tra password
        if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng.");
        }

        // 3) Cập nhật last_login_at
        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        // 4) Generate Access Token
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());

        // 5) Generate Refresh Token
        String refreshTokenString = java.util.UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenString)
                .expiredAt(OffsetDateTime.now().plusDays(30))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        // 6) Trả về AuthResponse
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
    /**
     * ======================================
     * REFRESH TOKEN
     * POST /api/auth/refresh
     * ======================================
     */
    public AuthResponse refreshAccessToken(String refreshTokenString) {

        // 1) Tìm refresh token trong DB
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ."));

        // 2) Kiểm tra token đã bị revoke chưa
        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new RuntimeException("Refresh token đã bị thu hồi.");
        }

        // 3) Kiểm tra token có hết hạn chưa
        if (refreshToken.getExpiredAt().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("Refresh token đã hết hạn.");
        }

        User user = refreshToken.getUser();

        // 4) Sinh accessToken mới
        String newAccessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail()
        );

        // 5) Trả về AuthResponse (dùng cùng refresh token)
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenString)  // giữ nguyên
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }

}
