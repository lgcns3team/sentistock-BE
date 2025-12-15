package com.example.SentiStock_backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;
import com.example.SentiStock_backend.notification.repository.NotificationRepository;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;
import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final PurchaseRepository purchaseRepository;
        private final StocksScoreRepository stocksScoreRepository;

        /**
         * 알림 생성
         */
        public void sendNotification(UserEntity user, CompanyEntity company, String content, String type) {

                NotificationEntity notification = NotificationEntity.builder()
                                .user(user)
                                .company(company)
                                .content(content)
                                .type(type)
                                .date(LocalDateTime.now())
                                .isCheck(false)
                                .build();

                notificationRepository.save(notification);

                // 추후: Firebase 알림 발송 가능
                // firebaseService.send(user.getFcmToken(), content);
        }

        /**
         * 유저의 알림 리스트 조회
         */
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
         * 알림 읽음 처리
         */
        public void checkNotification(Long notificationId, Long userId) {

                NotificationEntity notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification Not Found"));

                notification.setCheck(true);
                notificationRepository.save(notification);
        }

        /**
         * 수익률 변동 알림 생성
         */
        public void sendProfitNotification(
                        UserEntity user,
                        CompanyEntity company,
                        Double profitChange,
                        String type) {

                String content = "PROFIT_UP".equals(type)
                                ? company.getName() + " 수익률이 +" + profitChange + "% 이상 변동했습니다."
                                : company.getName() + " 수익률이 -" + profitChange + "% 이상 변동했습니다.";

                NotificationEntity notification = NotificationEntity.builder()
                                .user(user)
                                .company(company)
                                .content(content)
                                .type(type)
                                .profitChange(profitChange)
                                .sentiChange(0.0)
                                .date(LocalDateTime.now())
                                .isCheck(false)
                                .build();

                notificationRepository.save(notification);
        }

        public void checkUserSentimentAlert(Long userId) {
                checkUserSentimentAlert(userId, 10.0);
        }

        public void checkUserSentimentAlert(Long userId, Double sentiChange) {

                List<PurchaseEntity> purchases = purchaseRepository.findByUser_Id(userId);

                for (PurchaseEntity purchase : purchases) {

                        Double baseSenti = purchase.getPurSenti();
                        if (baseSenti == null)
                                continue;

                        StocksScoreEntity latestScore = stocksScoreRepository
                                        .findTopByCompany_IdOrderByDateDesc(
                                                        purchase.getCompany().getId())
                                        .orElse(null);

                        if (latestScore == null)
                                continue;

                        Double currentSenti = latestScore.getScore();
                        Double diff = Math.abs(currentSenti - baseSenti);

                        if (diff >= sentiChange) {
                                sendSentimentNotification(
                                                purchase.getUser(),
                                                purchase.getCompany(),
                                                baseSenti,
                                                currentSenti,
                                                sentiChange);
                        }
                }
        }

        /**
         * 감정 점수 변화 알림 생성
         */
        public void sendSentimentNotification(
                        UserEntity user,
                        CompanyEntity company,
                        Double baseSenti,
                        Double currentSenti,
                        Double sentiChange) {

                String content = company.getName()
                                + " 감정 점수가 매수 당시 대비 "
                                + sentiChange + " 이상 변동했습니다."
                                + " (기준: " + baseSenti
                                + ", 현재: " + currentSenti + ")";

                NotificationEntity notification = NotificationEntity.builder()
                                .user(user)
                                .company(company)
                                .content(content)
                                .type("SENTIMENT_CHANGE") // ✅ 통일
                                .profitChange(0.0)
                                .sentiChange(sentiChange)
                                .date(LocalDateTime.now())
                                .isCheck(false)
                                .build();

                notificationRepository.save(notification);
        }

}
