package com.example.SentiStock_backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;
import com.example.SentiStock_backend.notification.repository.NotificationRepository;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;


@Service
@RequiredArgsConstructor
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final FirebaseService firebaseService;

        

        /**
         * 알림 조회
         **/
        public List<NotificationResponseDto> getNotifications(Long userId) {

                return notificationRepository.findByUser_IdOrderByDateDesc(userId)
                                .stream()
                                .map(n -> NotificationResponseDto.builder()
                                                .id(n.getId())
                                                .content(n.getContent())
                                                .type(n.getType())
                                                .isCheck(n.isCheck())
                                                .companyId(n.getCompany().getId())
                                                .date(n.getDate())
                                                .build())
                                .toList();
        }

        /**
         * 알림 확인 처리
         **/
        public void checkNotification(Long notificationId, Long userId) {

                NotificationEntity notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification Not Found"));

                if (!notification.getUser().getId().equals(userId)) {
                        throw new RuntimeException("Access Denied");
                }

                notification.setCheck(true);
                notificationRepository.save(notification);
        }


        public void sendNotification(
                        UserEntity user,
                        CompanyEntity company,
                        NotificationType type,
                        StockEvent event) {

                // 알림 타입별 메시지 생성
                String content = buildContent(company, type, event);

                // 중복 알림 방지
                boolean exists = notificationRepository
                                .existsByUser_IdAndCompany_IdAndTypeAndIsCheckFalse(
                                                user.getId(),
                                                company.getId(),
                                                type.name());

                if (exists) {
                        return;
                }

                // 알림 저장
                NotificationEntity notification = NotificationEntity.builder()
                                .user(user)
                                .company(company)
                                .content(content)
                                .type(type.name())
                                .date(LocalDateTime.now())
                                .isCheck(false)
                                .build();

                notificationRepository.save(notification);

                // FCM 전송
                if (user.getFcmToken() != null) {
                        firebaseService.sendPush(
                                        user.getFcmToken(),
                                        "센티스톡 알림",
                                        content);
                }
        }

        private String buildContent(
                        CompanyEntity company,
                        NotificationType type,
                        StockEvent event) {
                return switch (type) {
                        case SELL -> company.getName()
                                        + " 수익률이 "
                                        + String.format("%.2f", event.getProfitRate())
                                        + "%로 매도 기준에 도달했습니다.";

                        case WARNING -> company.getName()
                                        + " 수익률이 "
                                        + String.format("%.2f", event.getProfitRate())
                                        + "%로 하락해 주의가 필요합니다.";

                        case INTEREST -> company.getName()
                                        + " 감정 점수가 "
                                        + String.format("%.2f", event.getSentimentChange())
                                        + " 만큼 변동했습니다.";

                        default -> "";
                };
        }

}
