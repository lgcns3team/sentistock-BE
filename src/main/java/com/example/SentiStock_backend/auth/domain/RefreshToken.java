package com.example.SentiStock_backend.auth.domain;

import com.example.SentiStock_backend.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "refresh_tokens",
    indexes = {
        @Index(name = "idx_refresh_token_user", columnList = "user_id"),
        @Index(name = "uk_refresh_token_token", columnList = "token", unique = true)
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 누구의 토큰
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 실제 Refresh Token 문자열 
    @Column(name = "token", nullable = false, length = 500)
    private String token;

    // 만료 시각
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    // 토큰 무효화 여부(로그아웃,재발급)
    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }
}
