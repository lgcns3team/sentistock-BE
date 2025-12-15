package com.example.SentiStock_backend.purchase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.notification.service.NotificationService;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseAlertService {

    private final PurchaseRepository purchaseRepository;
    private final StockRepository stockRepository;
    private final NotificationService notificationService;

    public void checkUserProfitAlert(Long userId, Double profitChange) {

        List<PurchaseEntity> purchases =
                purchaseRepository.findByUser_Id(userId);

        for (PurchaseEntity purchase : purchases) {

            CompanyEntity company = purchase.getCompany();

            StockEntity latestStock = stockRepository
                    .findTopByCompanyIdOrderByDateDesc(company.getId())
                    .orElse(null);

            if (latestStock == null) continue;

            double avgPrice = purchase.getAvgPrice();
            double currentPrice = latestStock.getStckPrpr();

            double profitRate =
                    ((currentPrice - avgPrice) / avgPrice) * 100;

            if (Math.abs(profitRate) >= profitChange) {

                String type = profitRate > 0
                        ? "PROFIT_UP"
                        : "PROFIT_DOWN";

                notificationService.sendProfitNotification(
                        purchase.getUser(),
                        company,
                        profitChange,
                        type
                );
            }

            // ⭐ 여기서 감정 점수 알림도 같이 처리 가능
            checkSentimentAlert(purchase);
        }
    }

    private void checkSentimentAlert(PurchaseEntity purchase) {
        // pur_senti 기준 ±10 변화 로직
    }
}
