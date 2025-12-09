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

    private String nickname;

    private String currentPassword;   // 현재 비밀번호
    private String newPassword;       // 새 비밀번호
}
