package com.example.SentiStock_backend.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SentiStock_backend.notification.domain.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser_IdOrderByDateDesc(Long userId);

    Optional<NotificationEntity> findTopByUser_IdOrderByDateDesc(Long userId);

    Optional<NotificationEntity> findTopByUser_IdAndTypeOrderByDateDesc(
        Long userId, String type);

    boolean existsByUser_IdAndCompany_IdAndTypeAndIsCheckFalse(
        Long userId,
        String companyId,
        String type
    );
    
    void deleteAllByUser_Id(Long userId);

}
