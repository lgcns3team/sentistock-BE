package com.example.SentiStock_backend.notification.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.notification.domain.dto.NotificationSettingRequestDto;
import com.example.SentiStock_backend.notification.service.NotificationSettingService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSettingCtrl {

    private final NotificationSettingService notificationSettingService;

    @Operation(
        summary = "알림 기준값 변경",
        description = "수익률/감정 점수 알림 기준값을 변경합니다. "
                    + "미입력 항목은 기존 값 유지됩니다."
    )
    @PatchMapping("/settings")
    public ResponseEntity<Void> updateSetting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NotificationSettingRequestDto request) {

        notificationSettingService.updateSetting(userDetails.getId(), request);

        return ResponseEntity.ok().build();
    }
}
