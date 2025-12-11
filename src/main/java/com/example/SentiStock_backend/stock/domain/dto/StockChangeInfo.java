package com.example.SentiStock_backend.stock.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StockChangeInfo {

    private Long currentPrice;

    private Double changeRate;

    private long volume;
}