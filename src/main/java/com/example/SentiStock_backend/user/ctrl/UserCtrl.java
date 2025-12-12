package com.example.SentiStock_backend.user.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.favorite.domain.dto.FavoriteCompanyResponseDto;
import com.example.SentiStock_backend.favorite.domain.dto.FavoriteSectorResponseDto;
import com.example.SentiStock_backend.favorite.service.FavoriteCompanyService;
import com.example.SentiStock_backend.purchase.service.PurchaseService;
import com.example.SentiStock_backend.user.domain.dto.OnboardingRequestDto;
import com.example.SentiStock_backend.user.domain.dto.PasswordChangeRequestDto;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.domain.dto.UserPurchaseResponseDto;
import com.example.SentiStock_backend.user.domain.dto.UserProfileUpdateRequestDto;
import com.example.SentiStock_backend.user.service.UserService;
import com.example.SentiStock_backend.favorite.service.FavoriteSectorService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserCtrl {

    private final UserService userService;
    private final FavoriteCompanyService favoriteCompanyService;
    private final FavoriteSectorService favoriteSectorService;
    private final PurchaseService purchaseService;


    //  내 정보 조회
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 아이디, 닉네임, 이메일, provider 등을 조회.")
    @GetMapping("/me")
    public UserMeResponseDto getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUserId();
        return userService.getMyInfo(userId);
    }


    //  내 기본 정보 수정 (닉네임만) - 회원정보 수정 탭
    @Operation(
        summary = "내 기본 정보 수정(닉네임)",
        description = "마이페이지 > 회원정보 수정 탭에서 사용하는 API. 닉네임만 수정."
    )
    @PatchMapping("/me/profile")
    public UserMeResponseDto updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserProfileUpdateRequestDto request) {
        String userId = userDetails.getUserId();
        return userService.updateMyNickname(userId, request.getNickname());
    }


    //  비밀번호 변경 - 계정 보안 탭 (로컬 회원만)
    @Operation(
        summary = "비밀번호 변경",
        description = "마이페이지 > 계정 보안 탭에서 사용하는 API. 로컬 회원만 비밀번호 변경 가능하며, 카카오 회원은 서버에서 차단."
    )
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PasswordChangeRequestDto request) {

        String userId = userDetails.getUserId();
        userService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }

  
    //  내 즐겨찾기 종목 목록 조회
    @Operation(
        summary = "내 즐겨찾기 종목 목록 조회",
        description = "로그인한 사용자가 즐겨찾기한 종목의 코드(id)와 이름(name)을 반환합니다."
    )
    @GetMapping("/me/favorites/companies")
    public List<FavoriteCompanyResponseDto> getMyFavoriteCompanies(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return favoriteCompanyService.getMyFavoriteCompanies(userId);
    }


    //  온보딩 완료 처리
    @Operation(
        summary = "온보딩 완료 (설문 + 관심 섹터 등록)",
        description = "카카오/로컬 사용자 모두 설문 점수와 관심섹터 5개를 저장하여 투자성향과 관심 섹터를 확정합니다."
                + " 카카오톡 회원가입 직후 Response Body에 onboardingRequired: true 일때 이 api를 호출."
    )
    @PatchMapping("/me/onboarding")
    public ResponseEntity<Void> completeOnboarding(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody OnboardingRequestDto request) {
        userService.completeOnboarding(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }


    //  내 구매 종목 목록 조회
    @Operation(
        summary = "내 구매 종목 목록 조회",
        description = "로그인한 사용자가 구매한 종목(코드, 회사명)과 평단가(avgPrice)를 반환합니다."
    )
    @GetMapping("/me/purchases")
    public List<UserPurchaseResponseDto> getMyPurchaseCompanies(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId(); 
        return purchaseService.getMyPurchases(userId);
    }

    //  내 관심 섹터 목록 조회
    @Operation(summary = "내 관심 섹터 목록 조회", description = "로그인한 사용자가 관심있어하는 섹터 목록(섹터명)을 반환합니다.")
    @GetMapping("/me/favorites/sectors")
    public List<FavoriteSectorResponseDto> getMyFavoriteSectors(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return favoriteSectorService.getMyFavoriteSectors(userId);
    }

    //  회원 탈퇴
    @Operation(
    summary = "회원 탈퇴",
    description = "로그인한 사용자의 계정을 삭제 "
                + "즐겨찾기, 구매내역, 리프레시 토큰, 선택한 섹터 등 연관 데이터도 함께 삭제 "
                + "카카오 회원은 서비스 연동 해제"
    )
    @DeleteMapping("/me/delete")   
    public ResponseEntity<Void> deleteMyAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String userId = userDetails.getUserId();  
        userService.deleteMyAccount(userId);
        return ResponseEntity.noContent().build(); 
    }

}
