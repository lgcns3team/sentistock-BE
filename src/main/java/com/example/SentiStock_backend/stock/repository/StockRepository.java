package com.example.SentiStock_backend.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SentiStock_backend.stock.domain.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findTopByCompanyIdOrderByDateDesc(String companyId);
}
