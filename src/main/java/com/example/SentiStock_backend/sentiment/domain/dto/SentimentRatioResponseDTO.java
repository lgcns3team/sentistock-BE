package com.example.SentiStock_backend.sentiment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SentimentRatioResponseDTO {

    private String companyId;
    private Long totalCount;
    private int positiveRatio;
    private int negativeRatio;
    private int neutralRatio;
}
