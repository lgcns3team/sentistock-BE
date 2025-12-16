package com.example.SentiStock_backend.notification.repository;

import com.example.SentiStock_backend.notification.domain.entity.NotificationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NotificationSettingRepository
        extends JpaRepository<NotificationSettingEntity, Long> {

    Optional<NotificationSettingEntity> findByUser_Id(Long userId);
}
