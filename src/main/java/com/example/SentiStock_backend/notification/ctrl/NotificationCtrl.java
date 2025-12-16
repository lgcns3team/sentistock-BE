package com.example.SentiStock_backend.notification.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "알림 관련 API")
public class NotificationCtrl {

    private final NotificationService notificationService;

    @Operation(summary = "내 알림 목록 조회", description = "로그인한 유저의 알림 목록을 불러옵니다.")
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 변경합니다.")
    @PatchMapping("/{notificationId}/check")
    public ResponseEntity<Void> checkNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId) {

        notificationService.checkNotification(notificationId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profit")
    @Operation(summary = "유저 수익률 변동 알림 생성", description = "사용자가 입력한 변동률 기준으로 보유한 모든 종목의 수익률을 검사하여 알림을 생성합니다.")
    public ResponseEntity<Void> createUserProfitAlert(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        notificationService.checkUserProfitAlert(userDetails.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sentiment")
    @Operation(summary = "유저 감정 점수 변동 알림 생성", description = "매수 시점 대비 감정 점수 변동이 기준치를 넘으면 알림을 생성합니다.")
    public ResponseEntity<Void> createUserSentimentAlert(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        notificationService.checkUserSentimentAlert(userDetails.getId());
        return ResponseEntity.ok().build();
    }

}
