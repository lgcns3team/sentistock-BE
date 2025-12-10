package com.example.SentiStock_backend.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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
    @Schema(
        description = "새 비밀번호 (영문+숫자+특수문자 8~12자)",
        example = "Newpass123!"
    )
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,12}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~12자여야 합니다."
    )
    private String newPassword;
    @Schema(description = "새 비밀번호 확인", example = "Newpass123!")
    private String confirmNewPassword;
}
