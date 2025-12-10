package com.example.SentiStock_backend.favorite.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteCompanyResponseDto {
    private String id;    // 종목코드
    private String name;  // 종목이름
}
