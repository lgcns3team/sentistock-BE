package com.example.SentiStock_backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.domain.type.NotificationType;
import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;

import com.example.SentiStock_backend.notification.repository.NotificationRepository;
import com.example.SentiStock_backend.trade.domain.type.TradeDecisionType;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final FirebaseService firebaseService;

        /*
         * =========================
         * 조회
         * =========================
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

        public void checkNotification(Long notificationId, Long userId) {
                NotificationEntity notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification Not Found"));

                if (!notification.getUser().getId().equals(userId)) {
                        throw new RuntimeException("Access Denied");
                }

                notification.setCheck(true);
                notificationRepository.save(notification);
        }

        /*
         * =========================
         * 충돌 차단 (시간 기준)
         * =========================
         */
        public boolean isRecentlySent(
                        Long userId,
                        String companyId,
                        NotificationType type,
                        int minutes) {

                Optional<NotificationEntity> last = notificationRepository
                                .findTopByUser_IdAndCompany_IdAndTypeOrderByDateDesc(
                                                userId,
                                                companyId,
                                                type.name());

                if (last.isEmpty())
                        return false;

                long diffMinutes = Duration.between(
                                last.get().getDate(),
                                LocalDateTime.now()).toMinutes();

                return diffMinutes < minutes;
        }

        /*
         * =========================
         * 알림 전송
         * =========================
         */
        public void sendNotification(
                        UserEntity user,
                        CompanyEntity company,
                        NotificationType type,
                        StockEvent event,
                        TradeDecisionType decision) {

                String content = buildContent(company, type, event, decision);
                if (content.isBlank())
                        return;

                NotificationEntity notification = NotificationEntity.builder()
                                .user(user)
                                .company(company)
                                .content(content)
                                .type(type.name())
                                .date(LocalDateTime.now())
                                .isCheck(false)
                                .build();

                notificationRepository.save(notification);

                if (user.getFcmToken() != null) {
                        firebaseService.sendPush(
                                        user.getFcmToken(),
                                        "센티스톡 알림",
                                        content);
                }
        }

        /*
         * =========================
         * 알림 문구 생성
         * =========================
         */
        private String buildContent(
                        CompanyEntity company,
                        NotificationType type,
                        StockEvent event,
                        TradeDecisionType decision) {

                String name = company.getName();
                Double sentiment = event.getSentimentChange();
                Double profit = event.getProfitRate();

                return switch (type) {

                        case BUY -> {
                                if (sentiment == null)
                                        yield "";
                                yield name + " 투자 심리가 개선되고 있습니다. "
                                                + String.format("(감정 점수 +%.0f) ", sentiment)
                                                + "매수를 고려해보세요.";
                        }

                        case SELL -> {

                                if (profit != null) {
                                        yield name + " 수익률이 "
                                                        + String.format("%.1f%%", profit)
                                                        + "에 도달했습니다. 차익 실현을 고려해보세요.";
                                }

                                if (decision == TradeDecisionType.SELL_DISTRIBUTION_TOP) {
                                        yield name + " 거래량이 급증하며 과열 신호가 감지되었습니다. "
                                                        + "차익 실현을 고려해보세요.";
                                }

                                if (decision == TradeDecisionType.SELL_MOMENTUM_WEAKENING) {
                                        yield name + " 거래량 감소와 함께 상승 동력이 약화되고 있습니다. "
                                                        + "매도를 고려해보세요.";
                                }

                                yield "";
                        }

                        case WARNING -> {
                                if (profit == null)
                                        yield "";
                                yield name + " 수익률이 "
                                                + String.format("%.1f%%", profit)
                                                + "로 하락했습니다. 주의가 필요합니다.";
                        }

                        case INTEREST -> {
                                if (sentiment == null)
                                        yield "";
                                yield name + " 시장 관심이 줄어들고 있습니다. "
                                                + String.format("(감정 점수 %.0f) ", sentiment)
                                                + "관망을 고려해보세요.";
                        }

                        default -> "";
                };
        }
}
