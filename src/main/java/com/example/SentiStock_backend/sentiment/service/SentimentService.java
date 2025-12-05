package com.example.SentiStock_backend.sentiment.service;

import com.example.SentiStock_backend.news.domain.entity.NewsEntity;
import com.example.SentiStock_backend.news.repository.NewsRepository;
import com.example.SentiStock_backend.sentiment.domain.dto.SentimentResponseDTO;
import com.example.SentiStock_backend.sentiment.domain.dto.StocksScoreResponseDTO;
import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.SentimentRepository;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentimentService {

        private final SentimentRepository sentimentRepository;
        private final NewsRepository newsRepository;
        private final StocksScoreRepository stocksScoreRepository;


        /**
         * 종목 감정 점수 평균 계산
         */
        public Double getCompanySentimentScore(String companyId) {

                List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);
                if (newsList.isEmpty())
                        return 0.0;

                List<String> newsIds = newsList.stream()
                                .map(n -> n.getId().toString())
                                .toList();

                List<SentimentEntity> sentimentList = sentimentRepository.findByNewsIdInOrderByDateDesc(newsIds);

                if (sentimentList.isEmpty())
                        return 0.0;

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
                if (newsList.isEmpty())
                        return List.of();

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
         * 감정 히스토리 조회 (최근 7개 ASC 정렬)
         */
        public List<StocksScoreResponseDTO> getSentimentHistory(String companyId) {

                List<StocksScoreEntity> list = stocksScoreRepository.findTop7ByCompanyIdOrderByDateDesc(companyId);

                return list.stream()
                                .sorted((a, b) -> a.getDate().compareTo(b.getDate())) // ASC 정렬
                                .map(StocksScoreResponseDTO::fromEntity)
                                .toList();
        }

}
