package com.example.SentiStock_backend.notification.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationCtrl {

    private final NotificationService notificationService;

    /**
     * 유저 알림 목록 조회
     */
    @Operation(summary = "내 알림 목록 조회", description = "로그인한 유저의 알림 목록을 불러옵니다.")
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    /**
     * 알림 읽음 처리
     */
    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 변경합니다.")
    @PatchMapping("/{notificationId}/check")
    public ResponseEntity<Void> checkNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId) {

        notificationService.checkNotification(notificationId, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
