package com.example.SentiStock_backend.notification.decision;

import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import org.springframework.stereotype.Service;

@Service
public class NotificationDecisionService {

    public NotificationType decide(
            StockEvent event,
            String investorType
    ) {
        Double profitRate = event.getProfitRate();
        Double sentimentChange = event.getSentimentChange();

        double profitThreshold =
                getProfitThresholdByInvestorType(investorType);

        double sentimentDelta =
                getSentimentDelta(investorType);

        // 매도
        if (profitRate != null && profitRate >= profitThreshold) {
            return NotificationType.SELL;
        }

        // 경고
        if (profitRate != null
                && profitRate <= -(profitThreshold * 0.5)) {
            return NotificationType.WARNING;
        }

        // 관심
        if (sentimentChange != null
                && Math.abs(sentimentChange) >= sentimentDelta) {
            return NotificationType.INTEREST;
        }

        return null;
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
