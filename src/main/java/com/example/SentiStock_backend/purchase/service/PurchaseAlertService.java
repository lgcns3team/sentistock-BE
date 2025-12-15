package com.example.SentiStock_backend.purchase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.repository.NotificationRepository;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;

@Service
@RequiredArgsConstructor
public class PurchaseAlertService {

    private final PurchaseRepository purchaseRepository;
    private final StockRepository stockRepository;
    private final StocksScoreRepository stocksScoreRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public void checkUserAlerts(Long userId) {

        NotificationEntity setting =
                notificationRepository
                    .findTopByUser_IdAndTypeOrderByDateDesc(userId, "SETTING")
                    .orElse(null);

        double profitChange = setting != null ? setting.getProfitChange() : 10.0;
        double sentiChange  = setting != null ? setting.getSentiChange()  : 10.0;

        List<PurchaseEntity> purchases =
                purchaseRepository.findByUser_Id(userId);

        for (PurchaseEntity purchase : purchases) {
            checkProfitAlert(purchase, profitChange);
            checkSentimentAlert(purchase, sentiChange);
        }
    }

    /**
     *  수익률 알림 판단
     */
    private void checkProfitAlert(PurchaseEntity purchase, double profitChange) {

        Float avgPrice = purchase.getAvgPrice();
        if (avgPrice == null || avgPrice <= 0) return;

        String companyId = purchase.getCompany().getId();

        StockEntity latestStock = stockRepository
                .findTopByCompanyIdOrderByDateDesc(companyId)
                .orElse(null);

        if (latestStock == null) return;

        double currentPrice = latestStock.getStckPrpr();
        double profitRate =
                ((currentPrice - avgPrice) / avgPrice) * 100;

        if (Math.abs(profitRate) < profitChange) return;

        String type = profitRate > 0
                ? "PROFIT_UP"
                : "PROFIT_DOWN";

        notificationService.sendProfitNotification(
                purchase.getUser(),
                purchase.getCompany(),
                profitChange,
                type
        );
    }

    /**
     *  감정 점수 알림 판단
     */
    private void checkSentimentAlert(PurchaseEntity purchase, double sentiChange) {

        Double baseSenti = purchase.getPurSenti();
        if (baseSenti == null) return;

        String companyId = purchase.getCompany().getId();

        StocksScoreEntity latestScore = stocksScoreRepository
                .findTopByCompany_IdOrderByDateDesc(companyId)
                .orElse(null);

        if (latestScore == null) return;

        Double currentSenti = latestScore.getScore();

        double diff = currentSenti - baseSenti;

        if (Math.abs(diff) < sentiChange) return;

        notificationService.sendSentimentNotification(
                purchase.getUser(),
                purchase.getCompany(),
                baseSenti,
                currentSenti,
                sentiChange
        );
    }
}
