package com.restaurant.api.security.filter;

import com.restaurant.api.entity.User;
import com.restaurant.api.repository.UserRepository;
import com.restaurant.api.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Collections;

import java.io.IOException;

/**
 * Filter đọc token từ header Authorization,
 * giải mã JWT và set user vào SecurityContext
 */
@Component("jwtAuthenticationFilter")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Lấy header Authorization
        final String authHeader = request.getHeader("Authorization");

        // Nếu không có token → đi tiếp (chọn lọc bằng SecurityConfig)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Tách chuỗi token
            final String token = authHeader.substring(7);

            // Giải mã token để lấy userId (subject)
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtService.getSigningKey())  // sử dụng key từ JwtService
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.getSubject());

            // Tìm user trong database
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Tạo Authentication cho Spring Security (dù không phân quyền, vẫn phải có list rỗng)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.emptyList()  // <= QUAN TRỌNG: KHÔNG ĐỂ null
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception ex) {
            // Token lỗi -> không authenticate
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
