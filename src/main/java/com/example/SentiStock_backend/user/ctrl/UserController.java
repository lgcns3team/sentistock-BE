package com.example.SentiStock_backend.user.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.favorite.domain.dto.FavoriteCompanyResponseDto;
import com.example.SentiStock_backend.user.domain.dto.OnboardingRequestDto;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.domain.dto.UserUpdateRequestDto;
import com.example.SentiStock_backend.user.service.UserService;
import com.example.SentiStock_backend.favorite.service.FavoriteCompanyService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final FavoriteCompanyService favoriteCompanyService;

    @Operation(
            summary = "내 정보 조회",
            description = "로그인한 사용자의 아이디, 닉네임, 이메일 등을 조회. "
    )
    @GetMapping("/me")
    public UserMeResponseDto getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String userId = userDetails.getUserId();
        return userService.getMyInfo(userId);
    }

    // 회원정보 수정 (닉네임 + 비밀번호)
    @Operation(
            summary = "내 정보 수정",
            description = "사이트 자체 회원은 닉네임,비밀번호 수정 가능. 카카오 회원은 닉네임만 수정 가능."
    )
    @PatchMapping("/me/update")
    public UserMeResponseDto updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        String userId = userDetails.getUserId();
        return userService.updateMyInfo(userId, request);
    }
    
    // 즐찾 조회
    @Operation(
        summary = "내 즐겨찾기 종목 목록 조회",
        description = "로그인한 사용자가 즐겨찾기한 종목의 코드(id)와 이름(name)을 반환합니다."
    )
    @GetMapping("/me/favorites/companies")
    public List<FavoriteCompanyResponseDto> getMyFavoriteCompanies(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        return favoriteCompanyService.getMyFavoriteCompanies(userId);
    }

    // 온보딩 완료 처리
    @Operation(summary = "온보딩 완료 (설문 + 관심 섹터 등록)",
               description = "카카오/로컬 사용자 모두 설문 점수와 관심섹터 5개를 저장하여 투자성향과 관심 섹터를 확정합니다.")
    @PatchMapping("/me/onboarding")
    public ResponseEntity<Void> completeOnboarding(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody OnboardingRequestDto request
    ) {
        userService.completeOnboarding(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

}
