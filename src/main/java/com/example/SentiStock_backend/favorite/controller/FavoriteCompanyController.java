package com.example.SentiStock_backend.favorite.controller;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.favorite.service.FavoriteCompanyService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class FavoriteCompanyController {

    private final FavoriteCompanyService favoriteCompanyService;


    @GetMapping("/{companyId}/favorite")
    public FavoriteStatusResponse getFavoriteStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String companyId
    ) {
        Long userId = userDetails.getId();
        boolean favorite = favoriteCompanyService.isFavorite(userId, companyId);
        return new FavoriteStatusResponse(favorite);
    }


    @PostMapping("/{companyId}/favorite/star")
    public FavoriteStatusResponse toggleFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String companyId
    ) {
        Long userId = userDetails.getId();
        boolean favorite = favoriteCompanyService.toggleFavorite(userId, companyId);
        return new FavoriteStatusResponse(favorite);
    }

    @Getter
    @AllArgsConstructor
    static class FavoriteStatusResponse {
        private boolean favorite;  // true면 별 채우고, false면 빈 별로 프론트에서 구현하면 댈듯
    }
}
