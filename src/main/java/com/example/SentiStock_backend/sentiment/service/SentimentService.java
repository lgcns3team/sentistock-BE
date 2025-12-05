package com.example.SentiStock_backend.sentiment.service;

import com.example.SentiStock_backend.news.repository.NewsRepository;
import com.example.SentiStock_backend.sentiment.repository.SentimentRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import com.example.SentiStock_backend.news.domain.entity.NewsEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final NewsRepository newsRepository;
    private final SentimentRepository sentimentRepository;

    /**
     * 종목 감정 점수 도출(평균 score)
     */
    public Double getCompanySentimentScore(Long companyId) {

        List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);

        if (newsList.isEmpty()) return 0.0;

        double total = 0;
        int count = 0;

        for (NewsEntity news : newsList) {
            List<SentimentEntity> sentiments =
                    sentimentRepository.findByNewsId(news.getId().toString());

            for (SentimentEntity s : sentiments) {
                if (s.getScore() != null) {
                    total += s.getScore();
                    count++;
                }
            }
        }

        return count > 0 ? total / count : 0.0;
    }

}
