package com.restaurant.api.service;

import com.restaurant.api.dto.auth.ForgotPasswordRequest;
import com.restaurant.api.dto.auth.ResetPasswordRequest;
import com.restaurant.api.entity.PasswordResetToken;
import com.restaurant.api.entity.User;
import com.restaurant.api.repository.PasswordResetTokenRepository;
import com.restaurant.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Xử lý Forgot Password + Reset Password
 */
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * ============================
     * FORGOT PASSWORD
     * ============================
     */
    public void forgotPassword(ForgotPasswordRequest request) {

        // Email có tồn tại hay không → vẫn trả "OK" để tránh lộ thông tin user
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            return; // không làm gì, tránh leak thông tin
        }

        // Tạo mã token ngẫu nhiên
        String token = UUID.randomUUID().toString();

        // Token hết hạn sau 15 phút
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiredAt(OffsetDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        // Tạo URL reset password (FE sẽ dùng sau này)
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        // Gửi email (mock)
        emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);
    }

    /**
     * ============================
     * RESET PASSWORD
     * ============================
     */
    public void resetPassword(ResetPasswordRequest request) {

        // 1) Tìm token
        PasswordResetToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ."));

        // 2) Kiểm tra hết hạn
        if (token.getExpiredAt().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn.");
        }

        // 3) Kiểm tra đã sử dụng chưa
        if (Boolean.TRUE.equals(token.getUsed())) {
            throw new RuntimeException("Token đã được sử dụng.");
        }

        // 4) Kiểm tra mật khẩu
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        User user = token.getUser();

        // 5) Mã hóa mật khẩu mới
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 6) Đánh dấu token đã sử dụng
        token.setUsed(true);
        tokenRepository.save(token);
    }
}
