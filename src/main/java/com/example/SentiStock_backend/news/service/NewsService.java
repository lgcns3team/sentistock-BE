package com.example.SentiStock_backend.news.service;

import com.example.SentiStock_backend.news.domain.dto.NewsSentimentDTO;
import com.example.SentiStock_backend.news.domain.entity.NewsEntity;
import com.example.SentiStock_backend.news.repository.NewsRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import com.example.SentiStock_backend.sentiment.repository.SentimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final SentimentRepository sentimentRepository;

    /** 최신 뉴스 3개 + 감정 점수 반환 */
    public List<NewsSentimentDTO> getRecentNewsWithSentiment(String companyId) {

        List<NewsEntity> newsList = newsRepository.findByCompanyIdOrderByDateDesc(companyId);

        return newsList.stream()
                .limit(3)
                .map(news -> {
                    SentimentEntity sentiment = sentimentRepository
                            .findTopByNewsIdOrderByDateDesc(news.getId())
                            .orElse(null);

                    return NewsSentimentDTO.builder()
                            .newsId(news.getId())
                            .title(news.getTitle())
                            .score(sentiment != null ? sentiment.getScore() : null)
                            .url(news.getUrl())
                            .build();
                })
                .toList();
    }
}
