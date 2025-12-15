package com.example.SentiStock_backend.notification.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfitAlertRequestDto {

    private Double profitChange;  // 변동률 기준 (예: 10)
}
