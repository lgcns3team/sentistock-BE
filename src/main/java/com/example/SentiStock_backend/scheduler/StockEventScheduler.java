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

    /**
     * 테스트용: 1분 주기
     */
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

        String companyId = purchase.getCompany().getId();

        StockEntity latestStock =
                stockRepository.findTopByCompanyIdOrderByDateDesc(companyId)
                        .orElse(null);

        StocksScoreEntity latestScore =
                stocksScoreRepository.findTopByCompany_IdOrderByDateDesc(companyId)
                        .orElse(null);

        if (latestStock == null || latestScore == null) return;

        double profitRate =
                calculateProfitRate(
                        purchase.getAvgPrice(),
                        latestStock.getStckPrpr()
                );

        double currentSentiment = latestScore.getScore();

        checkProfitEvent(purchase, profitRate);
        checkSentimentEvent(purchase, currentSentiment);
        checkVolumeEvent(purchase);
    }

    /*
     * ==========================
     * PROFIT 이벤트 (독립)
     * ==========================
     */
    private void checkProfitEvent(PurchaseEntity purchase, double profitRate) {

        double last = safe(purchase.getLastProfitEventRate());

        if (Math.abs(profitRate - last) < 3.0) return;

        if (purchase.getLastProfitEventAt() != null &&
                Duration.between(
                        purchase.getLastProfitEventAt(),
                        LocalDateTime.now()
                ).toMinutes() < 3) return;

        StockEventType type =
                profitRate > last
                        ? StockEventType.PROFIT_UP
                        : StockEventType.PROFIT_DOWN;

        StockEvent event = StockEvent.builder()
                .type(type)
                .userId(purchase.getUser().getId())
                .companyId(purchase.getCompany().getId())
                .profitRate(profitRate)
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, event.getCompanyId(), event);

        purchase.setLastProfitEventRate(profitRate);
        purchase.setLastProfitEventAt(LocalDateTime.now());
        purchaseRepository.save(purchase);
    }

    /*
     * ==========================
     * SENTIMENT 이벤트 (단독)
     * ==========================
     */
    private void checkSentimentEvent(
            PurchaseEntity purchase,
            double currentSentiment
    ) {

        double base =
                purchase.getLastSentiEventScore() != null
                        ? purchase.getLastSentiEventScore()
                        : purchase.getPurSenti();

        double change = currentSentiment - base;

        if (Math.abs(change) < 10.0) return;

        if (purchase.getLastSentiEventAt() != null &&
                Duration.between(
                        purchase.getLastSentiEventAt(),
                        LocalDateTime.now()
                ).toMinutes() < 3) return;

        StockEventType type =
                change > 0
                        ? StockEventType.SENTIMENT_UP
                        : StockEventType.SENTIMENT_DOWN;

        StockEvent event = StockEvent.builder()
                .type(type)
                .userId(purchase.getUser().getId())
                .companyId(purchase.getCompany().getId())
                .sentimentChange(change)
                .currentSentiment(currentSentiment)
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, event.getCompanyId(), event);

        purchase.setLastSentiEventScore(currentSentiment);
        purchase.setLastSentiEventAt(LocalDateTime.now());
        purchaseRepository.save(purchase);
    }

    /*
     * ==========================
     * VOLUME 이벤트 (복합 판단 트리거)
     * ==========================
     */
    private void checkVolumeEvent(PurchaseEntity purchase) {

        String companyId = purchase.getCompany().getId();

        List<StockEntity> stocks =
                stockRepository.findTop5ByCompanyIdOrderByDateDesc(companyId);

        if (stocks.size() < 5) return;

        long today = stocks.get(0).getAcmlVol();

        double avg =
                stocks.subList(1, 5).stream()
                        .mapToLong(StockEntity::getAcmlVol)
                        .average()
                        .orElse(0);

        if (avg <= 0) return;

        double volumeRate = ((today - avg) / avg) * 100;

        if (Math.abs(volumeRate) < 50.0) return;

        StockEvent event = StockEvent.builder()
                .type(StockEventType.VOLUME)
                .userId(purchase.getUser().getId())
                .companyId(companyId)
                .volumeRate(volumeRate)
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, companyId, event);
    }

    /*
     * ==========================
     * Utils
     * ==========================
     */
    private double calculateProfitRate(Float avgPrice, double currentPrice) {
        if (avgPrice == null || avgPrice <= 0) return 0.0;
        return ((currentPrice - avgPrice) / avgPrice) * 100;
    }

    private double safe(Double v) {
        return v == null ? 0.0 : v;
    }
}
