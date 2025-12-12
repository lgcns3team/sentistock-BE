package com.example.SentiStock_backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;
import com.example.SentiStock_backend.notification.domain.dto.NotificationResponseDto;
import com.example.SentiStock_backend.notification.repository.NotificationRepository;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

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

        // 다른 유저가 읽음 처리 못하도록 방지
        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access Denied");
        }

        notification.setCheck(true);
        notificationRepository.save(notification);
    }
}
