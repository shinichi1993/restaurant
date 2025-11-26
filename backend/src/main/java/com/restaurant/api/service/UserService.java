package com.restaurant.api.service;

import com.restaurant.api.entity.User;
import com.restaurant.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service lấy User đang đăng nhập (từ SecurityContext)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Lấy user hiện tại từ SecurityContext (set bởi JWT Filter)
     */
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        return null;
    }
    /**
     * Đổi mật khẩu cho user đang đăng nhập
     */
    public void changePassword(User user, String oldPassword, String newPassword, String confirmPassword, PasswordEncoder encoder) {

        // 1) Kiểm tra old password
        if (!encoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu cũ không đúng.");
        }

        // 2) Kiểm tra confirm
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        // 3) Mã hoá mật khẩu mới
        user.setPasswordHash(encoder.encode(newPassword));
        userRepository.save(user);
    }

}
