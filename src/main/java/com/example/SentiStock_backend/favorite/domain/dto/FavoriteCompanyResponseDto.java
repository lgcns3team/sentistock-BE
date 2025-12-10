package com.example.SentiStock_backend.favorite.domain.dto;

import java.util.List;

import com.example.SentiStock_backend.stock.domain.dto.StockChangeInfo;
import com.example.SentiStock_backend.stock.domain.dto.StockPriceDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteCompanyResponseDto {
    private String id;    // 종목코드
    private String name;  // 종목이름
    private List<StockPriceDto> price; // 캔들그래프를 위한 데이터
    private StockChangeInfo change; // 전일대비 등락률, 현재가
    private int sentiScore; // 감정점수
}
