package com.example.SentiStock_backend.purchase.repository;

import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    List<PurchaseEntity> findByUser_Id(Long userId);
    void deleteAllByUser_Id(Long userId);

    Optional<PurchaseEntity> findByUser_IdAndCompany_Id(Long userId, String companyId);
}
