package com.example.SentiStock_backend.notification.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationSettingRequestDto {

    private Double profitChange; 
    private Double sentiChange;  
}
