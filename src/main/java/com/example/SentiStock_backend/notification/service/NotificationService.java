package com.example.SentiStock_backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;
import com.example.SentiStock_backend.notification.repository.NotificationRepository;
import com.example.SentiStock_backend.notification.service.NotificationSettingService;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;
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

    /* ======================
       1. 알림 조회
       ====================== */
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

    /* ======================
       2. 알림 읽음 처리
       ====================== */
    public void checkNotification(Long notificationId, Long userId) {

        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification Not Found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access Denied");
        }

        notification.setCheck(true);
        notificationRepository.save(notification);
    }

    /* ======================
       3. 수익률 알림 트리거
       ====================== */
    public void checkUserProfitAlert(Long userId) {

        double profitChange = notificationSettingService.getProfitChange(userId);

        List<PurchaseEntity> purchases = purchaseRepository.findByUser_Id(userId);

        for (PurchaseEntity purchase : purchases) {

            Float avgPrice = purchase.getAvgPrice();
            if (avgPrice == null || avgPrice <= 0) continue;

            StockEntity latestStock = stockRepository
                    .findTopByCompanyIdOrderByDateDesc(
                            purchase.getCompany().getId())
                    .orElse(null);

            if (latestStock == null) continue;

            double currentPrice = latestStock.getStckPrpr();
            double profitRate = ((currentPrice - avgPrice) / avgPrice) * 100;

            if (Math.abs(profitRate) < profitChange) continue;

            String type = profitRate > 0 ? "PROFIT_UP" : "PROFIT_DOWN";

            String content = purchase.getCompany().getName()
                    + " 수익률이 "
                    + String.format("%.2f", profitRate)
                    + "% 변동했습니다.";

            saveNotification(
                    purchase,
                    content,
                    type
            );
        }
    }

    /* ======================
       4. 감정 점수 알림 트리거
       ====================== */
    public void checkUserSentimentAlert(Long userId) {

        double sentiChange = notificationSettingService.getSentiChange(userId);

        List<PurchaseEntity> purchases = purchaseRepository.findByUser_Id(userId);

        for (PurchaseEntity purchase : purchases) {

            Double baseSenti = purchase.getPurSenti();
            if (baseSenti == null) continue;

            StocksScoreEntity latestScore = stocksScoreRepository
                    .findTopByCompany_IdOrderByDateDesc(
                            purchase.getCompany().getId())
                    .orElse(null);

            if (latestScore == null) continue;

            Double currentSenti = latestScore.getScore();
            double diff = Math.abs(currentSenti - baseSenti);

            if (diff < sentiChange) continue;

            String content = purchase.getCompany().getName()
                    + " 감정 점수가 매수 당시 대비 "
                    + diff + " 만큼 변했습니다.";

            saveNotification(
                    purchase,
                    content,
                    "SENTIMENT_CHANGE"
            );
        }
    }

    /* ======================
       공통 알림 저장
       ====================== */
    private void saveNotification(
            PurchaseEntity purchase,
            String content,
            String type) {

        NotificationEntity notification = NotificationEntity.builder()
                .user(purchase.getUser())
                .company(purchase.getCompany())
                .content(content)
                .type(type)
                .date(LocalDateTime.now())
                .isCheck(false)
                .build();

        notificationRepository.save(notification);
    }
}
