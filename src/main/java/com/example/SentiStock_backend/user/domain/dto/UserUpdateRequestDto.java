package com.example.SentiStock_backend.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {

    // 닉네임
    private String nickname;
    // 새 비밀번호 
    private String newPassword;
}
