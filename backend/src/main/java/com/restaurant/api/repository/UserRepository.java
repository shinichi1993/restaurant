package com.restaurant.api.repository;

import com.restaurant.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository cho bảng users.
 * Hỗ trợ tìm user theo email.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email dùng cho login + register
    Optional<User> findByEmail(String email);

    // Kiểm tra email tồn tại
    boolean existsByEmail(String email);
}
