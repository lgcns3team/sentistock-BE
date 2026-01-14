package com.example.SentiStock_backend.sentiment.domain.dto;

import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StocksScoreResponseDto {

    private LocalDateTime date;
    private Double score;

    public static StocksScoreResponseDto fromEntity(StocksScoreEntity entity) {
        return StocksScoreResponseDto.builder()
                .date(entity.getDate())
                .score(entity.getScore())
                .build();
    }
}
