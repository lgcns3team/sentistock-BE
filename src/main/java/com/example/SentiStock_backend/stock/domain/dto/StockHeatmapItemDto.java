package com.example.SentiStock_backend.stock.domain.dto;

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
public class StockHeatmapItemDto {

    private String companyId;      // 종목코드
    private String companyName;    // 종목명

    private Long currentPrice;     // 현재가
    private Double changeRate;     // 등락률
}