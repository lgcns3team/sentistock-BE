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
    public NotificationType decide(StockEvent event, String investorType) {

        // 수익률 기준 판단 (매도 / 경고)
        if (event.getProfitRate() != null) {
            double threshold = getProfitThresholdByInvestorType(investorType);

            if (event.getProfitRate() >= threshold) {
                log.info("[DECISION] SELL triggered (profitRate={})", event.getProfitRate());
                return NotificationType.SELL;
            }

            if (event.getProfitRate() <= -threshold) {
                log.info("[DECISION] WARNING triggered (profitRate={})", event.getProfitRate());
                return NotificationType.WARNING;
            }
        }

        // 감정 점수 변화 → 관심 알림
        if (event.getSentimentChange() != null) {
            double deltaThreshold = getSentimentDelta(investorType);

            if (Math.abs(event.getSentimentChange()) >= deltaThreshold) {
                log.info("[DECISION] INTEREST triggered (sentimentChange={})",
                        event.getSentimentChange());
                return NotificationType.INTEREST;
            }
        }

        // 아무 조건도 안 맞으면 알림 없음
        return NotificationType.NONE;


    }

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

    private double getProfitThresholdByInvestorType(String investorType) {
        if (investorType == null) return 10.0;

        switch (investorType) {
            case "안정형": return 4.0;
            case "안정추구형": return 7.0;
            case "위험중립형": return 10.0;
            case "적극투자형": return 20.0;
            case "공격투자형": return 30.0;
            default: return 10.0;
        }
    }
}
