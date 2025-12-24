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

    // 1분 주기 (테스트용) → 운영 시 5분 / 10분
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

        // 최신 주가
        StockEntity latestStock = stockRepository
                .findTopByCompanyIdOrderByDateDesc(purchase.getCompany().getId())
                .orElse(null);

        // 최신 감정 점수
        StocksScoreEntity latestScore = stocksScoreRepository
                .findTopByCompany_IdOrderByDateDesc(purchase.getCompany().getId())
                .orElse(null);

        if (latestStock == null || latestScore == null) {
            return;
        }

        // 수익률 계산
        double profitRate = calculateProfitRate(
                purchase.getAvgPrice(),
                latestStock.getStckPrpr()
        );

        // 감정 변화 계산
        double sentimentChange = latestScore.getScore() - purchase.getPurSenti();

        // 이벤트 발행 조건 판단
        if (!shouldPublishEvent(purchase, profitRate, latestScore.getScore())) {
            return;
        }

        // Kafka 이벤트 생성
        StockEvent event = StockEvent.builder()
                .userId(purchase.getUser().getId())
                .companyId(purchase.getCompany().getId())
                .profitRate(profitRate)
                .sentimentChange(sentimentChange)
                .occurredAt(LocalDateTime.now())
                .build();

        // Kafka 발행
        kafkaTemplate.send(TOPIC, event);

        log.info(
                "StockEvent published | userId={} companyId={} profitRate={} sentiChange={}",
                event.getUserId(),
                event.getCompanyId(),
                profitRate,
                sentimentChange
        );

        // 기준 상태 업데이트 (중복 방지 핵심)
        purchase.setLastEventProfitRate(profitRate);
        purchase.setLastEventSenti(latestScore.getScore());
        purchase.setLastEventAt(LocalDateTime.now());

        purchaseRepository.save(purchase);
    }

    // ==========================
    // 계산 & 조건 메서드
    // ==========================

    private double calculateProfitRate(Float avgPrice, double currentPrice) {
        if (avgPrice == null || avgPrice <= 0) return 0.0;
        return ((currentPrice - avgPrice) / avgPrice) * 100;
    }

    private boolean shouldPublishEvent(
            PurchaseEntity purchase,
            double profitRate,
            double currentSenti
    ) {

        // 기준값
        double lastProfit = safe(purchase.getLastEventProfitRate());
        double lastSenti = safe(purchase.getLastEventSenti());

        boolean profitChangedEnough =
                Math.abs(profitRate - lastProfit) >= 3.0;

        boolean sentiChangedEnough =
                Math.abs(currentSenti - lastSenti) >= 10.0;

        // 쿨타임 (10분)
        boolean cooldownPassed =
                purchase.getLastEventAt() == null ||
                Duration.between(
                        purchase.getLastEventAt(),
                        LocalDateTime.now()
                ).toMinutes() >= 10;

        return cooldownPassed && (profitChangedEnough || sentiChangedEnough);
    }

    private double safe(Double value) {
        return value == null ? 0.0 : value;
    }
}
