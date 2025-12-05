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
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        /**
         * 종목 감정 점수 저장 (평균 감정 점수를 Stocks_score 테이블에 저장)
         */
        public void saveCompanySentimentScore(String companyId) {

                Double score = getCompanySentimentScore(companyId); // 평균 감정 점수 계산

                if (score == 0.0) {
                        log.warn("⚠ 감정 점수 없음 → 저장 스킵: {}", companyId);
                        return;
                }

                StocksScoreEntity entity = StocksScoreEntity.builder()
                                .companyId(companyId)
                                .score(score)
                                .date(LocalDateTime.now()) // 저장 시간
                                .build();

                stocksScoreRepository.save(entity);

                log.info("📌 감정 점수 저장 완료 → {} = {}", companyId, score);
        }

}
