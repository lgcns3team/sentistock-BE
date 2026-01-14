package com.example.SentiStock_backend.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {

    @Schema(description = "새 닉네임", example = "신난 투자왕")
    private String nickname;
}
