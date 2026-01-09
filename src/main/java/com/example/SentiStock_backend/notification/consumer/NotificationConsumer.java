package com.example.SentiStock_backend.notification.consumer;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.event.StockEventType;
import com.example.SentiStock_backend.notification.decision.NotificationDecisionService;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.sentiment.service.SentimentService;
import com.example.SentiStock_backend.stock.service.VolumeService;
import com.example.SentiStock_backend.trade.domain.service.TradeDecisionService;
import com.example.SentiStock_backend.trade.domain.type.TradeDecisionType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationDecisionService notificationDecisionService;
    private final TradeDecisionService tradeDecisionService;
    private final NotificationService notificationService;

    private final PurchaseRepository purchaseRepository;
    private final CompanyRepository companyRepository;

    private final SentimentService sentimentService;
    private final VolumeService volumeService;

    @KafkaListener(topics = "stock-events", groupId = "notification-group")
    @Transactional
    public void consume(StockEvent event) {

        if (event == null || event.getUserId() == null || event.getCompanyId() == null) {
            return;
        }

        PurchaseEntity purchase =
                purchaseRepository.findByUser_IdAndCompany_Id(
                        event.getUserId(),
                        event.getCompanyId()
                ).orElse(null);
        if (purchase == null) return;

        CompanyEntity company =
                companyRepository.findById(event.getCompanyId()).orElse(null);
        if (company == null) return;

        NotificationType notificationType = NotificationType.NONE;
        TradeDecisionType decision = null;

        /* =========================
         *  감정 단독 판단
         * ========================= */
        if (event.getType() == StockEventType.SENTIMENT_UP
                || event.getType() == StockEventType.SENTIMENT_DOWN) {

            notificationType =
                    notificationDecisionService.decideSentiment(event, purchase);
        }

        /* =========================
         *  감정 + 거래량 복합 판단
         * ========================= */
        if (event.getType() == StockEventType.SENTIMENT_UP
                || event.getType() == StockEventType.SENTIMENT_DOWN
                || event.getType() == StockEventType.VOLUME) {

            List<Double> recentSentiments =
                    sentimentService.getRecentSentiments(
                            event.getCompanyId(), 3);

            if (recentSentiments.size() >= 3) {

                double volumeRate =
                        volumeService.calculateVolumeRate(event.getCompanyId());

                decision = tradeDecisionService.decideSell(
                        purchase,
                        recentSentiments,
                        volumeRate
                );

                NotificationType complexType = mapToNotificationType(decision);

                if (complexType == NotificationType.SELL
                        || complexType == NotificationType.WARNING
                        || complexType == NotificationType.INTEREST) {

                    notificationType = complexType;
                }
            }
        }

        /* =========================
         *  수익률 알림
         * ========================= */
        if (event.getType() == StockEventType.PROFIT_UP) {
            notificationType = NotificationType.SELL;
        }

        if (event.getType() == StockEventType.PROFIT_DOWN) {
            notificationType = NotificationType.WARNING;
        }

        if (notificationType == NotificationType.NONE) return;

        /* =========================
         *  중복 차단
         * ========================= */
        boolean blocked = notificationService.isRecentlySent(
                purchase.getUser().getId(),
                company.getId(),
                notificationType,
                3
        );

        if (blocked) {
            log.info(
                    "[BLOCKED] duplicated notification | userId={}, companyId={}, type={}",
                    purchase.getUser().getId(),
                    company.getId(),
                    notificationType
            );
            return;
        }

        /* =========================
         *  알림 + 푸시 전송
         * ========================= */
        String fcmToken = purchase.getUser().getFcmToken();
        log.info(
                "[NOTI] ready to send | userId={}, type={}, fcmToken={}",
                purchase.getUser().getId(),
                notificationType,
                fcmToken != null ? "EXISTS" : "NULL"
        );

        notificationService.sendNotification(
                purchase.getUser(),
                company,
                notificationType,
                event,
                decision
        );

        log.info(
                "Notification sent | userId={}, companyId={}, type={}, decision={}",
                purchase.getUser().getId(),
                company.getId(),
                notificationType,
                decision
        );
    }

    private NotificationType mapToNotificationType(TradeDecisionType decision) {
        if (decision == null) return NotificationType.NONE;

        switch (decision) {
            case SELL_DISTRIBUTION_TOP:
            case SELL_MOMENTUM_WEAKENING:
                return NotificationType.SELL;
            case SELL_INTEREST_FADE:
                return NotificationType.INTEREST;
            case HOLD_PANIC:
                return NotificationType.WARNING;
            default:
                return NotificationType.NONE;
        }
    }
}
