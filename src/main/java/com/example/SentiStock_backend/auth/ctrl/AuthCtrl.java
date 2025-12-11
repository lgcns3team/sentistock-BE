package com.example.SentiStock_backend.auth.ctrl;

import com.example.SentiStock_backend.auth.domain.dto.*;
import com.example.SentiStock_backend.auth.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.auth.oauth.dto.KakaoTokenResponse;
import com.example.SentiStock_backend.auth.oauth.dto.KakaoUserInfoResponse;
import com.example.SentiStock_backend.auth.oauth.service.KakaoOAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthCtrl {

    private final AuthService authService;
    private final KakaoOAuthService kakaoOAuthService;

    // 회원가입
    @Operation(summary = "회원가입", description = "정보를 입력 후, 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류 또는 중복된 사용자"),
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDto request) {
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
    // 로그아웃
    @Operation(summary = "로그아웃", description = "로그아웃 처리")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("인증 정보가 없습니다.");
        }

        String userId = userDetails.getUserId();
        authService.logout(userId);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @Operation(summary = "카카오 유저정보 확인 (테스트용)", description = "인가 코드로 카카오 AccessToken을 받아 사용자 정보를 조회.")
    @GetMapping("/oauth/kakao-test")
    public ResponseEntity<?> kakaoTest(@RequestParam String code) {
        KakaoTokenResponse token = kakaoOAuthService.getKakaoToken(code);  
        KakaoUserInfoResponse userInfo = kakaoOAuthService.getUserInfo(token.getAccess_token());  
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "카카오 로그인", description = "인가 코드로 카카오 정보를 조회해 기존 회원은 로그인, 신규는 자동 회원가입 후 JWT 발급.")
    @GetMapping("/oauth/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam String code) {
        LoginResponseDto response = authService.kakaoLogin(code);
        return ResponseEntity.ok(response);
    }


}
