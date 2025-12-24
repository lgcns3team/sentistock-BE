package com.example.SentiStock_backend.notification.consumer;

import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.notification.decision.NotificationDecisionService;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.user.service.UserInvestorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

        private final NotificationDecisionService decisionService;
        private final UserInvestorService userInvestorService;
        private final NotificationService notificationService;

        @KafkaListener(topics = "stock-events", groupId = "notification-group")
        public void consume(StockEvent event) {

                Long userId = event.getUserId();

                if (userId == null) {
                        log.warn("⚠ userId is null. Skip event: {}", event);
                        return;
                }

                String investorType = userInvestorService.getInvestorType(userId);

                NotificationType type = decisionService.decide(event, investorType);

                if (type == null)
                        return;

                // 🔴 이 부분은 나중에 바꿀 예정이므로 지금은 주석 처리해도 됨
                /*
                 * notificationService.sendNotification(
                 * event.getCompanyId(),
                 * type,
                 * event
                 * );
                 */

                log.info(
                                "🔔 알림 판단 결과 userId={}, investorType={}, companyId={}, type={}",
                                userId,
                                investorType,
                                event.getCompanyId(),
                                type);
        }
}
