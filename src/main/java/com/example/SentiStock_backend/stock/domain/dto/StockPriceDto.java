package com.example.SentiStock_backend.stock.domain.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class StockPriceDto {

    private LocalDateTime date; // 봉 시작 시간
    private Long open;
    private Long high;
    private Long low;
    private Long close;
    private Long volume;
}
