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
public class StocksScoreResponseDTO {

    private LocalDateTime date;
    private Double score;

    public static StocksScoreResponseDTO fromEntity(StocksScoreEntity entity) {
        return StocksScoreResponseDTO.builder()
                .date(entity.getDate())
                .score(entity.getScore())
                .build();
    }
}
