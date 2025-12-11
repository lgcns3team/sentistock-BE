package com.example.SentiStock_backend.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordChangeRequestDto {

    @Schema(description = "현재 비밀번호", example = "Oldpass123!")
    @NotBlank(message = "현재 비밀번호를 입력하세요.")
    private String currentPassword;

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
    @NotBlank(message = "새 비밀번호 확인을 입력하세요.")
    private String confirmNewPassword;
}
