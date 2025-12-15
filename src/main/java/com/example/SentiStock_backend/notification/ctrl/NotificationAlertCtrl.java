package com.example.SentiStock_backend.notification.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.purchase.service.PurchaseAlertService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationAlertCtrl {

    private final PurchaseAlertService purchaseAlertService;

    /**
     * 수동 알림 체크 API
     */
    @Operation(
        summary = "수익률/감정 점수 알림 수동 체크",
        description = "로그인한 사용자의 매수 종목을 기준으로 "
                    + "수익률 및 감정 점수 변동 알림을 즉시 체크합니다."
    )
    @PostMapping("/check")
    public ResponseEntity<Void> checkMyAlerts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();

        purchaseAlertService.checkUserAlerts(userId);

        return ResponseEntity.ok().build();
    }
}
