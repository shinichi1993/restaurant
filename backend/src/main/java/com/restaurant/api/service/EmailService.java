package com.restaurant.api.service;

import org.springframework.stereotype.Service;

/**
 * Service gửi email (mock)
 * Chưa cần SMTP thật. Giai đoạn này chỉ log ra console.
 */
@Service
public class EmailService {

    public void sendResetPasswordEmail(String email, String resetUrl) {
        // Giai đoạn này chỉ MOCK — in ra console
        System.out.println("===== RESET PASSWORD EMAIL =====");
        System.out.println("To: " + email);
        System.out.println("Reset link: " + resetUrl);
        System.out.println("=================================");
    }
}
