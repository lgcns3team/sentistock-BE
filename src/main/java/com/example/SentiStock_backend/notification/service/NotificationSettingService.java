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

        /**
         * 알림 설정 조회
         * - profitChange만 반환
         */
        public NotificationSettingResponseDto getSetting(Long userId) {

                NotificationSettingEntity setting = settingRepository
                                .findByUser_Id(userId)
                                .orElseGet(() -> createDefaultSetting(userId));

                return new NotificationSettingResponseDto(
                                setting.getProfitChange());
        }

        /**
         * 알림 설정 수정
         * - profitChange만 수정 가능
         */
        @Transactional
        public void updateSetting(Long userId, NotificationSettingRequestDto request) {

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User Not Found"));

                if (!user.isSubscribe()) {
                        throw new IllegalStateException("Subscription required to update notification settings");
                }

                NotificationSettingEntity setting = settingRepository
                                .findByUser_Id(userId)
                                .orElseGet(() -> createDefaultSetting(userId));

                if (request.getProfitChange() != null) {
                        setting.setProfitChange(request.getProfitChange());
                }
        }

        /**
         * 기본 알림 설정 생성
         * - profitChange 기본값: 10
         */
        private NotificationSettingEntity createDefaultSetting(Long userId) {

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User Not Found"));

                NotificationSettingEntity setting = NotificationSettingEntity.builder()
                                .user(user)
                                .profitChange(10.0)
                                .build();

                return settingRepository.save(setting);
        }

        /**
         * 수익률 알림 기준값 조회
         * - 설정 없으면 기본 10
         */
        public Double getProfitChange(Long userId) {
                return settingRepository
                                .findByUser_Id(userId)
                                .map(NotificationSettingEntity::getProfitChange)
                                .orElse(10.0);
        }
}
