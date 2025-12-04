package com.example.SentiStock_backend.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;   // 항상 "Bearer"

    private String userId;
    private String nickname;
    private String investorType;
    private boolean subscribe;
}
