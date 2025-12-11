package com.example.SentiStock_backend.user.domain.dto; 

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPurchaseResponseDto {
    private String companyId;   // 종목코드
    private String companyName; // 회사명
    private Float avgPrice;     // 평단가
    private Long currentPrice;  // 현재가
    private Double profitRate;  // 수익률 (%)
}
