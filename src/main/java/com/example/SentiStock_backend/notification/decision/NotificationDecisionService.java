package com.example.SentiStock_backend.notification.decision;

import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.event.StockEventType;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationDecisionService {

    /**
     * 감정 점수 단독 판단
     *
     * - SENTIMENT_UP   → BUY
     * - SENTIMENT_DOWN → INTEREST
     */
    public NotificationType decideSentiment(
            StockEvent event,
            PurchaseEntity purchase
    ) {

        if (event.getType() == StockEventType.SENTIMENT_UP) {
            log.info("[DECISION] SENTIMENT_UP → BUY");
            return NotificationType.BUY;
        }

        if (event.getType() == StockEventType.SENTIMENT_DOWN) {
            log.info("[DECISION] SENTIMENT_DOWN → INTEREST");
            return NotificationType.INTEREST;
        }

        return NotificationType.NONE;
    }
}
