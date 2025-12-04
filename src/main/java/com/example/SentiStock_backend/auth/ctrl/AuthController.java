package com.example.SentiStock_backend.auth.ctrl;

import com.example.SentiStock_backend.auth.domain.dto.*;
import com.example.SentiStock_backend.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @Operation(summary = "회원가입", description = "정보를 입력 후, 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류 또는 중복된 사용자"),
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    // 로그인
    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호 오류"),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급", description = "만료된 AccessToken 과 유효한 RefreshToken 으로 새로운 토큰 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새로운 토큰 발급 성공"),
            @ApiResponse(responseCode = "400", description = "RefreshToken 오류 또는 만료"),
    })
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenReissueRequestDto request) {
        TokenResponseDto response = authService.reissue(request);
        return ResponseEntity.ok(response);
    }
}
