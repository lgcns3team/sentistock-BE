package com.example.SentiStock_backend.notification.decision;

import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationDecisionService {

    /**
     * 핵심 판단 메서드
     */
    public NotificationType decide(
            StockEvent event,
            String investorType,
            double profitChange
    ) {

        // ==========================
        // 수익률 판단 (DB 기준값 사용)
        // ==========================
        if (event.getProfitRate() != null) {

            if (event.getProfitRate() >= profitChange) {
                log.info("[DECISION] SELL triggered (profitRate={}, threshold={})",
                        event.getProfitRate(), profitChange);
                return NotificationType.SELL;
            }

            if (event.getProfitRate() <= -profitChange) {
                log.info("[DECISION] WARNING triggered (profitRate={}, threshold={})",
                        event.getProfitRate(), profitChange);
                return NotificationType.WARNING;
            }
        }

        // ==========================
        // 감정 점수 판단
        // ==========================
        if (event.getSentimentChange() != null &&
            event.getCurrentSentiment() != null) {

            if (!isInSentimentRange(
                    investorType,
                    event.getCurrentSentiment())) {
                return NotificationType.NONE;
            }

            double deltaThreshold = getSentimentDelta(investorType);

            if (Math.abs(event.getSentimentChange()) >= deltaThreshold) {
                return NotificationType.INTEREST;
            }
        }

        return NotificationType.NONE;
    }

    // ==========================
    // 감정 기준 정책
    // ==========================
    private double getSentimentDelta(String investorType) {
        switch (investorType) {
            case "안정형": return 10.0;
            case "안정추구형": return 15.0;
            case "위험중립형": return 20.0;
            case "적극투자형": return 25.0;
            case "공격투자형": return 30.0;
            default: return 10.0;
        }
    }

    private boolean isInSentimentRange(
            String investorType,
            double current) {

        switch (investorType) {
            case "안정형":
                return current >= 40 && current <= 80;
            case "안정추구형":
                return current >= 30 && current <= 80;
            case "위험중립형":
            case "적극투자형":
                return current >= 20 && current <= 90;
            case "공격투자형":
                return current >= 0 && current <= 100;
            default:
                return true;
        }
    }
}
