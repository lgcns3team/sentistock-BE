package com.example.SentiStock_backend.stock.domain.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
@Schema(description = "캔들 차트(봉 차트) 데이터")
public class StockPriceDto {

    @Schema(description = "봉 시작 시간", example = "2025-12-08T10:00:00")
    private LocalDateTime date;

    @Schema(description = "시가", example = "198500")
    private Long open;

    @Schema(description = "고가", example = "200000")
    private Long high;

    @Schema(description = "저가", example = "195000")
    private Long low;

    @Schema(description = "전일 종가", example = "198000")
    private Long close;

    @Schema(description = "거래량", example = "1000000")
    private Long volume;
}
