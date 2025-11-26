package com.restaurant.api.controller;

import com.restaurant.api.dto.user.CreateUserRequest;
import com.restaurant.api.entity.User;
import com.restaurant.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * USER MANAGEMENT – LIST USERS
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public User create(@RequestBody CreateUserRequest req) {

        // 1) Check email đã tồn tại
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // 2) Tạo user mới
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword())) // hash password
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User update(
            @PathVariable Long id,
            @RequestBody CreateUserRequest req
    ) {

        // 1. Tìm user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // 2. Update thông tin cơ bản
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());

        // 3. Email không cho đổi (để đơn giản)
        // Nếu bạn muốn cho đổi email → thêm check email duplicate

        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        // 1) Check user tồn tại
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // 2) Xoá user
        userRepository.delete(user);
    }


}
