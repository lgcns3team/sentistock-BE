package com.example.SentiStock_backend.user.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMeResponseDto {

    private Long id;            // PK
    private String userId;      // 로그인 아이디
    private String nickname;    // 닉네임
    private String userEmail;   // 이메일
    private String investorType;// 투자 성향
    private boolean subscribe;  // 구독 여부
}
