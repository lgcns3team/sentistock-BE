package com.example.SentiStock_backend.news.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsSentimentDTO {
    private Long newsId;
    private String title;
    private Long score;
    private String url;

}
