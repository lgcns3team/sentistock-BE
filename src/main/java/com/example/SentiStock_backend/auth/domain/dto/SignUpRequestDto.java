package com.example.SentiStock_backend.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    @Schema(description = "닉네임 (2~10자)", example = "센티스톡유저")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    @Schema(description = "아이디 (영문+숫자 8~12자)", example = "user1234")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$",
            message = "아이디는 영문과 숫자를 포함한 8~12자여야 합니다."
    )
    private String userId;

    @Schema(description = "비밀번호 (영문+숫자+특수문자 8~12자)", example = "password123!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,12}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~12자여야 합니다."
    )
    private String password;

    @Schema(description = "비밀번호 확인", example = "password123!")
    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    @Schema(description = "이메일", example = "user@example.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;

    // 설문 총점 
    @Schema(description = "투자 성향 설문 점수 합계", example = "27")
    @NotNull(message = "설문 점수를 입력해주세요.")
    private Integer investorScore;

}
