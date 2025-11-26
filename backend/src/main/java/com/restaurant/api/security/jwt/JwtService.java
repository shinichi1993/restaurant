package com.restaurant.api.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Service tạo & kiểm tra JWT Access Token
 * Module 01 – Authentication
 */
@Service
public class JwtService {

    // TODO: SECRET KEY phải đưa vào application.yml
    private static final String SECRET_KEY = "THIS_IS_TEST_SECRET_KEY_CHANGE_IT_123456789";
    private static final long EXPIRE_SECONDS = 900; // 15 phút

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /** Tạo AccessToken cho user */
    public String generateAccessToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRE_SECONDS * 1000);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // subject = userId
                .claim("email", email)              // thêm thông tin email
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getExpirationSeconds() {
        return EXPIRE_SECONDS;
    }
}
