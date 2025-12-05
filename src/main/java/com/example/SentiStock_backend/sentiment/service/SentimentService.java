package com.example.SentiStock_backend.sentiment.service;

import com.example.SentiStock_backend.news.domain.entity.NewsEntity;
import com.example.SentiStock_backend.news.repository.NewsRepository;
import com.example.SentiStock_backend.sentiment.domain.dto.SentimentResponseDTO;
import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import com.example.SentiStock_backend.sentiment.repository.SentimentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final SentimentRepository sentimentRepository;
    private final NewsRepository newsRepository;


    /**
     * 종목 감정 점수 평균 계산
     */
    public Double getCompanySentimentScore(String companyId) {

        List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);
        if (newsList.isEmpty()) return 0.0;

        List<String> newsIds = newsList.stream()
                .map(n -> n.getId().toString())
                .toList();

        List<SentimentEntity> sentimentList =
                sentimentRepository.findByNewsIdInOrderByDateDesc(newsIds);

        if (sentimentList.isEmpty()) return 0.0;

        return sentimentList.stream()
                .mapToDouble(s -> s.getScore() == null ? 0 : s.getScore())
                .average()
                .orElse(0.0);
    }


    /**
     * 최근 감정 점수 3개 조회
     */
    public List<SentimentResponseDTO> getRecent3Sentiments(String companyId) {

        List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);
        if (newsList.isEmpty()) return List.of();

        List<String> newsIds = newsList.stream()
                .map(n -> n.getId().toString())
                .toList();

        return sentimentRepository.findByNewsIdInOrderByDateDesc(newsIds)
                .stream()
                .limit(3)
                .map(SentimentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     * 감정 히스토리 조회 (최근 7일)
     * DB서 정렬 후 날짜 필터는 자바에서 수행
     */
    public List<SentimentResponseDTO> getSentimentHistory(String companyId) {

        List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);
        if (newsList.isEmpty()) return List.of();

        List<String> newsIds = newsList.stream()
                .map(n -> n.getId().toString())
                .toList();

        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        return sentimentRepository.findByNewsIdInOrderByDateDesc(newsIds)
                .stream()
                .filter(s -> {
                    try {
                        LocalDateTime date = LocalDateTime.parse(s.getDate());
                        return date.isAfter(weekAgo);
                    } catch (Exception e) {
                        return false; // 형식 변환 실패 데이터 제외
                    }
                })
                .sorted(Comparator.comparing(s -> LocalDateTime.parse(s.getDate())))
                .map(SentimentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
