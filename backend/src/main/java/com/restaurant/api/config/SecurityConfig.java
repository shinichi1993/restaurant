package com.restaurant.api.config;

import com.restaurant.api.security.filter.JwtAuthenticationFilter; // ✅ ĐÚNG FILTER HIỆN CÓ
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig – cấu hình Security cho Module 01
 * - Bật CORS cho FE (5173)
 * - Cho phép các API auth public
 * - Các API khác yêu cầu JWT
 * - Thêm JwtAuthenticationFilter vào filter chain
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // ✅ Dùng đúng filter đã tạo ở B4: JwtAuthenticationFilter
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Tắt CSRF vì dùng JWT
                .csrf(csrf -> csrf.disable())
                // Bật CORS cho frontend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // JWT filter phải đặt TRƯỚC authorizeHttpRequests
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Stateless session (chỉ dùng JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Phân quyền cho từng endpoint
                .authorizeHttpRequests(auth -> auth
                        // Các API auth cho phép public
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh").permitAll()
                        .requestMatchers("/api/auth/forgot-password").permitAll()
                        .requestMatchers("/api/auth/reset-password").permitAll()

                        // 👇 TẠM THỜI CHO PHÉP TỰ DO /api/users (dev cho Module 01)
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/dish/**").permitAll()
                        .requestMatchers("/api/ingredients/**").permitAll()
                        .requestMatchers("/api/stock-entries/**").permitAll()
                        .requestMatchers("/api/dishes/**").permitAll()
                        .requestMatchers("/api/orders/**").permitAll()
                        .requestMatchers("/api/invoices/**").permitAll()
                        .requestMatchers("/api/payments/**").permitAll()
                        .requestMatchers("/api/report/**").permitAll()
                        .requestMatchers("/api/settings/**").permitAll()

                        // Các API khác yêu cầu JWT
                        .anyRequest().authenticated()
                );


        /*
        http
                // Tắt CSRF vì dùng REST API
                .csrf(csrf -> csrf.disable())
                // Bật CORS cho frontend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Stateless session (không dùng session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // TẠM THỜI: Cho phép TẤT CẢ request, không kiểm tra JWT
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

         */

        // ✅ Quan trọng: gắn JwtAuthenticationFilter trước UsernamePasswordAuthenticationFilter
        //http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Cấu hình CORS cho phép FE (http://localhost:5173) gọi sang BE (http://localhost:8080)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Domain FE
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://restaurant-production-5799.up.railway.app"   // 👈 FE production
        ));
        // Các method cho phép
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép mọi header
        config.setAllowedHeaders(List.of("*"));
        // Cho phép gửi cookie / Authorization header
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cho toàn bộ endpoint
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * PasswordEncoder cho toàn hệ thống
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager dùng cho AuthService (login)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
