package com.example.SentiStock_backend.notification.consumer;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.notification.decision.NotificationDecisionService;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.notification.service.NotificationSettingService;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
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
        private final CompanyRepository companyRepository;
        private final NotificationSettingService notificationSettingService;

        @KafkaListener(topics = "stock-events", groupId = "notification-group")
        public void consume(StockEvent event) {

                if (event == null || event.getUserId() == null || event.getCompanyId() == null) {
                        log.warn("Invalid event payload: {}", event);
                        return;
                }

                log.info("Kafka StockEvent received: {}", event);

                try {
                        UserEntity user = userInvestorService.findById(event.getUserId());

                        CompanyEntity company = companyRepository
                                        .findById(event.getCompanyId())
                                        .orElse(null);

                        if (company == null) {
                                log.warn("Company not found. companyId={}", event.getCompanyId());
                                return;
                        }

                        // ⭐ 사용자 설정값 조회 (DB)
                        double profitChange = notificationSettingService.getProfitChange(user.getId());

                        // ⭐ 변경된 decide 호출
                        NotificationType type = decisionService.decide(
                                        event,
                                        user.getInvestorType(),
                                        profitChange);

                        if (type == NotificationType.NONE) {
                                return;
                        }

                        notificationService.sendNotification(user, company, type, event);

                        log.info("Notification processed. userId={}, companyId={}, type={}",
                                        user.getId(), company.getId(), type);

                } catch (Exception e) {
                        log.error("Kafka consume failed. event={}", event, e);
                }
        }

}
