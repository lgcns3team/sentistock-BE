package com.example.SentiStock_backend.user.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMeResponseDto {

    private Long id;             // PK
    private String userId;       // 아이디
    private String nickname;     // 닉네임
    private String userEmail;    // 이메일
    // 비밀번호는 실제 값 대신 마스킹된 문자열만 전달
    private String passwordMasked;
}
