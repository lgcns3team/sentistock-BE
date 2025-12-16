package com.example.SentiStock_backend.notification.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSettingResponseDto {

    private Double profitChange;
    private Double sentiChange;
}
