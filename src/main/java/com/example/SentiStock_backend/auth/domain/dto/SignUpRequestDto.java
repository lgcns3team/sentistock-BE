package com.example.SentiStock_backend.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    private String nickname;       // 닉네임
    private String userId;         // 로그인 아이디
    private String password;       // 비밀번호(평문)
    private String userEmail;      // 이메일
    private String investorType;   // 투자 성향 (안정형/위험중립형/적극/공격 등)
    private boolean subscribe;     // 구독 여부
}
