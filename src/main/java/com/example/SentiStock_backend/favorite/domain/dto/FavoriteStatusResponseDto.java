package com.example.SentiStock_backend.favorite.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteStatusResponseDto {
    private boolean favorite;  // true면 즐겨찾기됨, false면 해제됨
}
