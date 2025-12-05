package com.example.SentiStock_backend.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {

    private Long id;
    private String nickname;
    private String userId;
    private String userEmail;
    private String investorType;
    private boolean subscribe;
}
