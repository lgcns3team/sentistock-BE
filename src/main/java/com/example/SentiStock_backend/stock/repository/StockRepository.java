package com.example.SentiStock_backend.stock.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SentiStock_backend.stock.domain.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
        Optional<StockEntity> findTopByCompanyIdOrderByDateDesc(String companyId);

        List<StockEntity> findByCompany_IdAndDateGreaterThanEqualOrderByDateAsc(
                        String companyId,
                        LocalDateTime date);

        List<StockEntity> findByCompany_IdAndDateBetweenOrderByDateAsc(
                        String companyId,
                        LocalDateTime start,
                        LocalDateTime end);

        Optional<StockEntity> findTop1ByCompanyIdOrderByDateDesc(String companyId);

        /**
         * 특정 종목의 최근 N일 주가 데이터 조회 (날짜 DESC)
         */
        List<StockEntity> findTop5ByCompanyIdOrderByDateDesc(String companyId);
}
