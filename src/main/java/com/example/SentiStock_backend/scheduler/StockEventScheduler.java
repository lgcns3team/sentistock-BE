package com.example.SentiStock_backend.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.event.StockEventType;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventScheduler {

    private final PurchaseRepository purchaseRepository;
    private final StockRepository stockRepository;
    private final StocksScoreRepository stocksScoreRepository;
    private final KafkaTemplate<String, StockEvent> kafkaTemplate;

    private static final String TOPIC = "stock-events";

    // 1분 주기 (테스트용)
    @Scheduled(fixedDelay = 60_000)
    public void publishStockEvents() {

        List<PurchaseEntity> purchases = purchaseRepository.findAll();

        for (PurchaseEntity purchase : purchases) {
            try {
                processPurchase(purchase);
            } catch (Exception e) {
                log.error("Scheduler error. purchaseId={}", purchase.getId(), e);
            }
        }
    }

    private void processPurchase(PurchaseEntity purchase) {

        StockEntity latestStock = stockRepository
                .findTopByCompanyIdOrderByDateDesc(
                        purchase.getCompany().getId())
                .orElse(null);

        StocksScoreEntity latestScore = stocksScoreRepository
                .findTopByCompany_IdOrderByDateDesc(
                        purchase.getCompany().getId())
                .orElse(null);

        if (latestStock == null || latestScore == null) {
            return;
        }

        double profitRate = calculateProfitRate(
                purchase.getAvgPrice(),
                latestStock.getStckPrpr());

        double currentSenti = latestScore.getScore();

        // 완전히 분리
        checkProfitEvent(purchase, profitRate);
        checkSentimentEvent(purchase, currentSenti);
    }

    /*
     * ==========================
     * 수익 알림 판단
     * ==========================
     */
    private void checkProfitEvent(
            PurchaseEntity purchase,
            double profitRate) {
        double lastProfit = safe(purchase.getLastProfitEventRate());

        boolean changedEnough = Math.abs(profitRate - lastProfit) >= 3.0;

        boolean cooldownPassed = purchase.getLastProfitEventAt() == null ||
                Duration.between(
                        purchase.getLastProfitEventAt(),
                        LocalDateTime.now()).toMinutes() >= 3;

        if (!changedEnough || !cooldownPassed)
            return;

        StockEvent event = StockEvent.builder()
                .type(StockEventType.PROFIT)
                .userId(purchase.getUser().getId())
                .companyId(purchase.getCompany().getId())
                .profitRate(profitRate)
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, event);

        log.info(
                "[PROFIT] event published | userId={} companyId={} profitRate={}",
                event.getUserId(),
                event.getCompanyId(),
                profitRate);

        // 수익 기준만 업데이트
        purchase.setLastProfitEventRate(profitRate);
        purchase.setLastProfitEventAt(LocalDateTime.now());

        purchaseRepository.save(purchase);
    }
    
    /*
     * ==========================
     * 감정 점수 알림 판단
     * ==========================
     */
    private void checkSentimentEvent(
            PurchaseEntity purchase,
            double currentSenti) {
        // 기준점 선택
        double baseSenti = purchase.getLastSentiEventScore() != null
                ? purchase.getLastSentiEventScore()
                : purchase.getPurSenti();

        double sentiChange = currentSenti - baseSenti;

        boolean changedEnough = Math.abs(sentiChange) >= 10.0;

        boolean cooldownPassed = purchase.getLastSentiEventAt() == null ||
                Duration.between(
                        purchase.getLastSentiEventAt(),
                        LocalDateTime.now()).toMinutes() >= 3;

        if (!changedEnough || !cooldownPassed)
            return;

        StockEvent event = StockEvent.builder()
                .type(StockEventType.SENTIMENT)
                .userId(purchase.getUser().getId())
                .companyId(purchase.getCompany().getId())
                .sentimentChange(sentiChange)
                .currentSentiment(currentSenti)
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, event);

        // 기준점 이동
        purchase.setLastSentiEventScore(currentSenti);
        purchase.setLastSentiEventAt(LocalDateTime.now());

        purchaseRepository.save(purchase);
    }

    /*
     * ==========================
     * 공통 유틸
     * ==========================
     */
    private double calculateProfitRate(Float avgPrice, double currentPrice) {
        if (avgPrice == null || avgPrice <= 0)
            return 0.0;
        return ((currentPrice - avgPrice) / avgPrice) * 100;
    }

    private double safe(Double value) {
        return value == null ? 0.0 : value;
    }
}
