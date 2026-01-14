package com.example.SentiStock_backend.user.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscriptionInfoResponseDto {

    private boolean subscribed;    // 구독 여부
    private LocalDate subscribeAt;  // 구독 시작일 
}
