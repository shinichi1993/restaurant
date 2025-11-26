package com.restaurant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

/**
 * Entity tương ứng bảng refresh_tokens
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ nhiều RefreshToken thuộc về 1 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Chuỗi token
    @Column(nullable = false, columnDefinition = "TEXT")
    private String token;

    private String userAgent;
    private String ipAddress;

    @Column(nullable = false)
    private OffsetDateTime expiredAt;

    @Column(nullable = false)
    private Boolean revoked = false;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }
}
