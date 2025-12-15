package com.example.SentiStock_backend.notification.domain.dto;

import lombok.Getter;

@Getter
public class NotificationSettingRequestDto {

    private Double profitChange; 
    private Double sentiChange;  
}
