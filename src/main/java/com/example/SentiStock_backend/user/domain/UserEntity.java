package com.example.SentiStock_backend.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "uk_users_user_id", columnList = "user_id", unique = true),
        @Index(name = "uk_users_user_email", columnList = "user_email", unique = true),
        @Index(name = "uk_users_provider_provider_id", columnList = "provider, provider_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 닉네임
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    // 아이디
    @Column(name = "user_id", nullable = false, length = 50, unique = true)
    private String userId;

    // 암호화된 비밀번호 (KAKAO 유저는 null일 수도 있음)
    @Column(name = "user_pw", length = 100)
    private String userPw;

    // 이메일
    @Column(name = "user_email", nullable = false, length = 50, unique = true)
    private String userEmail;

    // 투자 성향
    @Column(name = "investor_type", length = 50)
    private String investorType;

    // 구독 여부
    @Column(name = "is_subscribe", nullable = false)
    private boolean isSubscribe;

    // 가입 타입: LOCAL / KAKAO / ...
    @Column(name = "provider", length = 20)
    private String provider;

    // 소셜 로그인에서 사용하는 provider별 고유 id
    @Column(name = "provider_id", length = 100)
    private String providerId;

    // === 도메인 메서드 ===

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String encodedPassword) {
        this.userPw = encodedPassword;
    }

    public void changeInvestorType(String investorType) {
        this.investorType = investorType;
    }

    public void changeSubscribe(boolean subscribe) {
        this.isSubscribe = subscribe;
    }

    public void changeProvider(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    // 분기용 – provider가 null/빈 값이면 LOCAL 취급
    public boolean isLocalUser() {
        return provider == null
                || provider.isBlank()
                || "LOCAL".equalsIgnoreCase(provider);
    }

    // 소셜인 경우 true (KAKAO, 향후 GOOGLE 등)
    public boolean isSocialUser() {
        return "KAKAO".equalsIgnoreCase(provider);
    }
}
