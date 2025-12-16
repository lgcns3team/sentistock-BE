package com.example.SentiStock_backend.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.example.SentiStock_backend.notification.domain.dto.NotificationSettingRequestDto;
import com.example.SentiStock_backend.notification.domain.dto.NotificationSettingResponseDto;
import com.example.SentiStock_backend.notification.domain.entity.NotificationSettingEntity;
import com.example.SentiStock_backend.notification.repository.NotificationSettingRepository;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

        private final NotificationSettingRepository settingRepository;
        private final UserRepository userRepository;
        private final NotificationSettingRepository notificationSettingRepository;

        /**
         * 알림 설정 조회
         */
        public NotificationSettingResponseDto getSetting(Long userId) {

                NotificationSettingEntity setting = settingRepository
                                .findByUser_Id(userId)
                                .orElseGet(() -> createDefaultSetting(userId));

                return new NotificationSettingResponseDto(
                                setting.getProfitChange(),
                                setting.getSentiChange());
        }

        /**
         * 알림 설정 저장 / 수정
         */
        @Transactional
        public void updateSetting(Long userId, NotificationSettingRequestDto request) {

                NotificationSettingEntity setting = settingRepository
                                .findByUser_Id(userId)
                                .orElseGet(() -> createDefaultSetting(userId));

                if (request.getProfitChange() != null) {
                        setting.setProfitChange(request.getProfitChange());
                }

                if (request.getSentiChange() != null) {
                        setting.setSentiChange(request.getSentiChange());
                }

                settingRepository.save(setting);
        }

        /**
         * 기본 설정 생성 (profit=10, senti=10)
         */
        private NotificationSettingEntity createDefaultSetting(Long userId) {

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User Not Found"));

                NotificationSettingEntity setting = NotificationSettingEntity.builder()
                                .user(user)
                                .profitChange(10.0)
                                .sentiChange(10.0)
                                .build();

                return settingRepository.save(setting);
        }

        /**
         * 감정 알림 기준값 조회 (없으면 기본 10)
         */
        public Double getSentiChange(Long userId) {
                return notificationSettingRepository
                                .findByUser_Id(userId)
                                .map(NotificationSettingEntity::getSentiChange)
                                .orElse(10.0);
        }

        /**
         * 수익률 알림 기준값 조회 (없으면 기본 10)
         */
        public Double getProfitChange(Long userId) {
                return notificationSettingRepository
                                .findByUser_Id(userId)
                                .map(NotificationSettingEntity::getProfitChange)
                                .orElse(10.0);
        }
}
