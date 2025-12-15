package com.example.SentiStock_backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.dto.NotificationSettingRequestDto;
import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.repository.NotificationRepository;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

        private final NotificationRepository notificationRepository;
        private final UserRepository userRepository;
        private final PurchaseRepository purchaseRepository;
        private final StocksScoreRepository stocksScoreRepository;

        @Transactional
        public void updateSetting(Long userId, NotificationSettingRequestDto request) {

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User Not Found"));

                NotificationEntity latest = notificationRepository
                                .findTopByUser_IdAndTypeOrderByDateDesc(userId, "SETTING")
                                .orElse(null);

                double profitChange = request.getProfitChange() != null
                                ? request.getProfitChange()
                                : (latest != null ? latest.getProfitChange() : 10.0);

                double sentiChange = request.getSentiChange() != null
                                ? request.getSentiChange()
                                : (latest != null ? latest.getSentiChange() : 10.0);

                NotificationEntity setting = NotificationEntity.builder()
                                .user(user)
                                .content("알림 기준값 변경")
                                .type("SETTING")
                                .profitChange(profitChange)
                                .sentiChange(sentiChange)
                                .isCheck(true)
                                .date(LocalDateTime.now())
                                .build();

                notificationRepository.save(setting);
        }

        /**
         * 감정 점수 변동 알림 (수동 테스트용)
         */
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

                        Double diff = Math.abs(latestScore.getScore() - baseSenti);

                        if (diff >= sentiChange) {
                                NotificationEntity notification = NotificationEntity.builder()
                                                .user(purchase.getUser())
                                                .company(purchase.getCompany())
                                                .content(
                                                                purchase.getCompany().getName()
                                                                                + " 감정 점수가 "
                                                                                + diff + "만큼 변했습니다.")
                                                .type("SENTIMENT_CHANGE")
                                                .sentiChange(sentiChange)
                                                .date(LocalDateTime.now())
                                                .isCheck(false)
                                                .build();

                                notificationRepository.save(notification);
                        }
                }
        }

}
