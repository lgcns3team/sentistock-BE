package com.example.SentiStock_backend.user.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.user.domain.dto.SubscriptionInfoResponseDto;
import com.example.SentiStock_backend.user.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription API", description = "사용자 구독 관리 관련 API")
public class SubscriptionCtrl {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "내 구독 상태 조회",
            description = "마이페이지 > 구독관리 탭에서 사용할 API. 구독 여부와 구독 시작일을 반환")
    @GetMapping("/me")
    public SubscriptionInfoResponseDto getMySubscription(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        return subscriptionService.getMySubscription(userId);
    }

    @Operation(summary = "구독 시작",
            description = "구독 시작 버튼 클릭 시 호출. 구독 여부를 true로 바꾸고 구독 시작일을 오늘로 설정")
    @PostMapping("/start")
    public SubscriptionInfoResponseDto startSubscription(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        return subscriptionService.startSubscription(userId);
    }

    @Operation(summary = "구독 해지",
            description = "구독 해지 버튼 클릭 시 호출. 구독을 중지")
    @PostMapping("/cancel")
    public SubscriptionInfoResponseDto cancelSubscription(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        return subscriptionService.cancelSubscription(userId);
    }
}
