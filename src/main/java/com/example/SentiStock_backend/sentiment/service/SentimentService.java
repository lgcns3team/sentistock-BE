package com.example.SentiStock_backend.sentiment.service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.news.domain.entity.NewsEntity;
import com.example.SentiStock_backend.news.repository.NewsRepository;
import com.example.SentiStock_backend.sentiment.domain.dto.SentimentRatioResponseDTO;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentService {

        private final SentimentRepository sentimentRepository;
        private final NewsRepository newsRepository;
        private final StocksScoreRepository stocksScoreRepository;
        private final CompanyRepository companyRepository;

        /**
         * 종목 감정 점수 평균 계산
         */
        public Double getCompanySentimentScore(String companyId) {

                List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);
                if (newsList.isEmpty())
                        return 0.0;

                List<Long> newsIds = newsList.stream()
                                .map(NewsEntity::getId)
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
         * 최근 기사 감정 점수 3개 조회
         */
        public List<SentimentResponseDTO> getRecent3Sentiments(String companyId) {

                List<NewsEntity> newsList = newsRepository.findByCompanyId(companyId);
                if (newsList.isEmpty())
                        return List.of();

                List<Long> newsIds = newsList.stream()
                                .map(NewsEntity::getId)
                                .toList();

                return sentimentRepository.findByNewsIdInOrderByDateDesc(newsIds)
                                .stream()
                                .limit(3)
                                .map(SentimentResponseDTO::fromEntity)
                                .toList();
        }

        /**
         * 종목 감정 히스토리 조회 (최근 7개 ASC 정렬)
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

                if (score == null) {
                        log.warn("감정 점수 없음 → 저장 스킵: {}", companyId);
                        return;
                }

                CompanyEntity company = companyRepository.findById(companyId)
                                .orElseThrow(() -> new IllegalArgumentException("회사 없음: " + companyId));

                StocksScoreEntity entity = StocksScoreEntity.builder()
                                .company(company)
                                .score(score)
                                .date(LocalDateTime.now())
                                .build();

                stocksScoreRepository.save(entity);

                log.info("감정 점수 저장 완료: {} = {}", companyId, score);
        }

        /**
         * 종목별 감정 비율 조회
         */
        public SentimentRatioResponseDTO getSentimentRatio(String companyId) {

                List<Object[]> resultList = sentimentRepository.getSentimentCountByCompany(companyId);

                if (resultList == null || resultList.isEmpty()) {
                        return new SentimentRatioResponseDTO(companyId, 0L, 0, 0, 0);
                }

                Object[] result = resultList.get(0);

                Long posCount = result[0] == null ? 0L : ((Number) result[0]).longValue();
                Long negCount = result[1] == null ? 0L : ((Number) result[1]).longValue();
                Long neuCount = result[2] == null ? 0L : ((Number) result[2]).longValue();
                Long totalCount = ((Number) result[3]).longValue();
                
                if (totalCount == 0) {
                        log.warn("⚠ totalCount is zero for companyId={}", companyId);
                        return new SentimentRatioResponseDTO(companyId, 0L, 0, 0, 0);
                }

                double posRatio = (posCount * 100.0) / totalCount;
                double negRatio = (negCount * 100.0) / totalCount;
                double neuRatio = (neuCount * 100.0) / totalCount;

                int posInt = (int) Math.round(posRatio);
                int negInt = (int) Math.round(negRatio);
                int neuInt = (int) Math.round(neuRatio);


                return new SentimentRatioResponseDTO(
                                companyId,
                                totalCount,
                                posInt,
                                negInt,
                                neuInt);
        }

}
