package com.example.SentiStock_backend.stock.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "섹터 히트맵에서 표시되는 종목 요약 정보")
public class StockHeatmapItemDto {

    @Schema(description = "종목 코드", example = "005930")
    private String companyId;
    
    @Schema(description = "종목명", example = "삼성전자")
    private String companyName;    

    @Schema(description = "현재가", example = "198500")
    private Long currentPrice;

    @Schema(description = "등락률(%)", example = "1.23")
    private Double changeRate;

    @Schema(description = "거래량")
    private long volume;
}