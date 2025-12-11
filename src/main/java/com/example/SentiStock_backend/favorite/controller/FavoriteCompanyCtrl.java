package com.example.SentiStock_backend.favorite.controller;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.favorite.domain.dto.FavoriteStatusResponseDto;
import com.example.SentiStock_backend.favorite.service.FavoriteCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class FavoriteCompanyCtrl {

    private final FavoriteCompanyService favoriteCompanyService;

    @Operation(summary = "종목 즐겨찾기 여부 조회",
            description = "사용자가 해당 종목을 즐겨찾기했는지 여부를 반환 (true/false).")
    @GetMapping("/{companyId}/favorite")
    public FavoriteStatusResponseDto getFavoriteStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String companyId
    ) {
        Long userId = userDetails.getId();
        boolean favorite = favoriteCompanyService.isFavorite(userId, companyId);
        return new FavoriteStatusResponseDto(favorite);
    }

    @Operation(
            summary = "종목 즐겨찾기",
            description = "즐겨찾기 버튼 클릭 시 호출 "
                        + "이미 true(즐찾상태)라면 false(해제), false(해제)라면 true(즐찾)로 설정 변경. "
                        + "반환되는 값은 변경된 최신 즐겨찾기 상태"
    )
    @PostMapping("/{companyId}/favorite/star")
    public FavoriteStatusResponseDto toggleFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String companyId
    ) {
        Long userId = userDetails.getId();
        boolean favorite = favoriteCompanyService.toggleFavorite(userId, companyId);
        return new FavoriteStatusResponseDto(favorite);
    }


}
