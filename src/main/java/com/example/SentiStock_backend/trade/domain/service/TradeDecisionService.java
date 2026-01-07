package com.example.SentiStock_backend.trade.domain.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.trade.domain.entitiy.TradeSignalEntity;
import com.example.SentiStock_backend.trade.domain.repository.TradeSignalRepository;
import com.example.SentiStock_backend.trade.domain.type.TradeDecisionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeDecisionService {

    private final PurchaseRepository purchaseRepository;
    private final TradeSignalRepository tradeSignalRepository;

    /**
     * 매도 판단 진입점
     */
    public TradeDecisionType decideSell(
            PurchaseEntity purchase,
            List<Double> recentSentiments,   // 최신 N개 (ex: 3개)
            double volumeRate                // 4일 평균 대비 %
    ) {

        // =========================
        // 1. 감정 점수 계산
        // =========================
        double currentSentiment =
                recentSentiments.get(recentSentiments.size() - 1);

        double maxSentiment =
                Collections.max(recentSentiments);

        double sentimentDelta =
                maxSentiment - currentSentiment;

        // =========================
        // 2. 감정 구간 판별
        // =========================
        boolean highZone = currentSentiment >= 60;
        boolean midZone = currentSentiment >= 40 && currentSentiment < 60;
        boolean lowZone = currentSentiment < 40;

        // =========================
        // 3. SELL-4 : 패닉 → 매도 금지
        // =========================
        if (lowZone && sentimentDelta >= 20 && volumeRate >= 5) {
            return saveSignal(
                    purchase,
                    TradeDecisionType.HOLD_PANIC,
                    currentSentiment,
                    sentimentDelta,
                    volumeRate
            );
        }

        // =========================
        // 4. SELL-2 : 과열 분배
        // =========================
        if (highZone && sentimentDelta >= 20 && volumeRate >= 5) {
            return saveSignal(
                    purchase,
                    TradeDecisionType.SELL_DISTRIBUTION_TOP,
                    currentSentiment,
                    sentimentDelta,
                    volumeRate
            );
        }

        // =========================
        // 5. SELL-1 : 상승 동력 약화
        // =========================
        if (highZone && sentimentDelta >= 20 && volumeRate <= -5) {
            return saveSignal(
                    purchase,
                    TradeDecisionType.SELL_MOMENTUM_WEAKENING,
                    currentSentiment,
                    sentimentDelta,
                    volumeRate
            );
        }

        // =========================
        // 6. SELL-3 : 관심 이탈
        // =========================
        if (midZone && sentimentDelta >= 10 && volumeRate <= -5) {
            return saveSignal(
                    purchase,
                    TradeDecisionType.SELL_INTEREST_FADE,
                    currentSentiment,
                    sentimentDelta,
                    volumeRate
            );
        }

        return TradeDecisionType.NONE;
    }

    // =========================
    // 공통 저장 처리
    // =========================
    private TradeDecisionType saveSignal(
            PurchaseEntity purchase,
            TradeDecisionType decisionType,
            double sentimentScore,
            double sentimentDelta,
            double volumeRate
    ) {

        LocalDateTime now = LocalDateTime.now();

        // 1️⃣ TradeSignal 저장
        TradeSignalEntity signal = TradeSignalEntity.builder()
                .userId(purchase.getUser().getId())
                .companyId(purchase.getCompany().getId())
                .decisionType(decisionType)
                .sentimentScore(sentimentScore)
                .sentimentDelta(sentimentDelta)
                .volumeRate(volumeRate)
                .createdAt(now)
                .build();

        tradeSignalRepository.save(signal);

        // 2️⃣ Purchase 상태 갱신 (중복 방지용)
        purchase.setLastSentiScore(sentimentScore);
        purchase.setLastVolumeEventRate(volumeRate);
        purchase.setLastSellRuleCode(decisionType.name());
        purchase.setLastSignalAt(now);

        purchaseRepository.save(purchase);

        return decisionType;
    }
}
