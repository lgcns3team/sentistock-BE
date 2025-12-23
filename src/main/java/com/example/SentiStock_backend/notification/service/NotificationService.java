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
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final NotificationSettingService notificationSettingService;
        private final PurchaseRepository purchaseRepository;
        private final StockRepository stockRepository;
        private final StocksScoreRepository stocksScoreRepository;
        private final FirebaseService firebaseService;

        private double getSentimentDelta(String investorType) {
                switch (investorType) {
                        case "안정형":
                                return 10.0;
                        case "안정추구형":
                                return 15.0;
                        case "위험중립형":
                                return 20.0;
                        case "적극투자형":
                                return 25.0;
                        case "공격투자형":
                                return 30.0;
                        default:
                }
                return 10.0;
        }

        private double getSentimentMin(String investorType) {
                switch (investorType) {
                        case "안정형":
                                return 40.0;
                        case "안정추구형":
                                return 30.0;
                        case "위험중립형":
                        case "적극투자형":
                                return 20.0;
                        case "공격투자형":
                                return 0.0;
                        default:
                                return 20.0;
                }
        }

        private double getSentimentMax(String investorType) {
                switch (investorType) {
                        case "안정형":
                        case "안정추구형":
                                return 80.0;
                        case "위험중립형":
                        case "적극투자형":
                                return 90.0;
                        case "공격투자형":
                                return 100.0;
                        default:
                                return 90.0;
                }
        }

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

        /**
         * 수익률 알림 트리거
         **/
        public void checkUserProfitAlert(Long userId) {

                double profitChange = notificationSettingService.getProfitChange(userId);

                List<PurchaseEntity> purchases = purchaseRepository.findByUser_Id(userId);

                for (PurchaseEntity purchase : purchases) {

                        Float avgPrice = purchase.getAvgPrice();
                        if (avgPrice == null || avgPrice <= 0)
                                continue;

                        StockEntity latestStock = stockRepository
                                        .findTopByCompanyIdOrderByDateDesc(
                                                        purchase.getCompany().getId())
                                        .orElse(null);

                        if (latestStock == null)
                                continue;

                        double currentPrice = latestStock.getStckPrpr();
                        double profitRate = ((currentPrice - avgPrice) / avgPrice) * 100;

                        if (Math.abs(profitRate) < profitChange)
                                continue;

                        String type = profitRate > 0 ? "PROFIT_UP" : "PROFIT_DOWN";

                        String content = purchase.getCompany().getName()
                                        + " 수익률이 "
                                        + String.format("%.2f", profitRate)
                                        + "% 변동했습니다.";

                        saveNotification(
                                        purchase,
                                        content,
                                        type);
                }
        }

        /**
         * 감정 점수 알림 트리거
         **/
        public void checkUserSentimentAlert(Long userId) {

                UserEntity user = purchaseRepository.findByUser_Id(userId)
                                .stream()
                                .findFirst()
                                .map(PurchaseEntity::getUser)
                                .orElseThrow(() -> new RuntimeException("User Not Found"));

                double sentiChange = getSentimentDelta(user.getInvestorType());

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

                        double diff = latestScore.getScore() - baseSenti;

                        if (Math.abs(diff) < sentiChange)
                                continue;

                        String type = diff > 0
                                        ? "SENTIMENT_SPIKE_UP"
                                        : "SENTIMENT_SPIKE_DOWN";

                        String content = purchase.getCompany().getName()
                                        + " 감정 점수가 매수 당시 대비 "
                                        + String.format("%.2f", Math.abs(diff))
                                        + " 만큼 "
                                        + (diff > 0 ? "상승" : "하락")
                                        + "했습니다.";

                        saveNotification(
                                        purchase,
                                        content,
                                        type);
                }
        }

        /**
         * 공통 알림 저장
         **/
        private void saveNotification(
                        PurchaseEntity purchase,
                        String content,
                        String type) {

                Long userId = purchase.getUser().getId();
                String companyId = purchase.getCompany().getId();

                boolean exists = notificationRepository
                                .existsByUser_IdAndCompany_IdAndTypeAndIsCheckFalse(
                                                userId,
                                                companyId,
                                                type);

                if (exists) {
                        return;
                }

                NotificationEntity notification = NotificationEntity.builder()
                                .user(purchase.getUser())
                                .company(purchase.getCompany())
                                .content(content)
                                .type(type)
                                .date(LocalDateTime.now())
                                .isCheck(false)
                                .build();

                notificationRepository.save(notification);

                String fcmToken = purchase.getUser().getFcmToken();
                if (fcmToken != null) {
                        firebaseService.sendPush(
                                        fcmToken,
                                        "센티스톡 알림",
                                        content);
                }

        }
}
