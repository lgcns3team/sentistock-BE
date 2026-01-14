package com.example.SentiStock_backend.sentiment.domain.dto;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentResponseDto {

    private Long id;
    private Double probPos;
    private Double probNeg;
    private Double probNeu;
    private String label;
    private Double score;
    private String date;
    private Long newsId;

    public static SentimentResponseDto fromEntity(SentimentEntity entity) {
        return SentimentResponseDto.builder()
                .id(entity.getId())
                .probPos(entity.getProbPos())
                .probNeg(entity.getProbNeg())
                .probNeu(entity.getProbNeu())
                .label(entity.getLabel())
                .score(entity.getScore())
                .date(entity.getDate())
                .newsId(entity.getNews() != null ? entity.getNews().getId() : null)
                .build();
    }
}
