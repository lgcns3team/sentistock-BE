package com.example.SentiStock_backend.purchase.repository;

import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
}
