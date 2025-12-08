package com.example.SentiStock_backend.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "uk_users_user_id", columnList = "user_id", unique = true),
        @Index(name = "uk_users_user_email", columnList = "user_email", unique = true)
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

    // 암호화된 비밀번호
    @Column(name = "user_pw", nullable = false, length = 100)
    private String userPw;

    // 이메일
    @Column(name = "user_email", nullable = false, length = 50, unique = true)
    private String userEmail;

    // 투자 성향 
    @Column(name = "investor_type", nullable = false, length = 50)
    private String investorType;

    // 구독 여부
    @Column(name = "is_subscribe", nullable = false)
    private boolean isSubscribe;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", length = 100)
    private String providerId;


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

    @PrePersist
    public void prePersist() {
        if (this.provider == null) {
            this.provider = "LOCAL";
        }
    }
}
