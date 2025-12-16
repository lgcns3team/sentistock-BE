package com.example.SentiStock_backend.notification.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.notification.domain.dto.NotificationSettingRequestDto;
import com.example.SentiStock_backend.notification.domain.dto.NotificationSettingResponseDto;
import com.example.SentiStock_backend.notification.service.NotificationSettingService;

@RestController
@RequestMapping("/api/notifications/settings")
@RequiredArgsConstructor
public class NotificationSettingCtrl {

    private final NotificationSettingService notificationSettingService;

    @Operation(summary = "알림 설정 조회", description = "로그인한 유저의 알림 설정을 불러옵니다.")
    @GetMapping
    public ResponseEntity<NotificationSettingResponseDto> getSetting(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(
                notificationSettingService.getSetting(userDetails.getId()));
    }

    @Operation(summary = "알림 설정 수정", description = "로그인한 유저의 알림 설정을 수정합니다.")
    @PatchMapping
    public ResponseEntity<Void> updateSetting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NotificationSettingRequestDto request) {

        notificationSettingService.updateSetting(
                userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }
}
