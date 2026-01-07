package com.example.SentiStock_backend.trade.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.SentiStock_backend.trade.domain.entitiy.TradeSignalEntity;
import com.example.SentiStock_backend.trade.domain.type.TradeDecisionType;

@Repository
public interface TradeSignalRepository extends JpaRepository<TradeSignalEntity, Long> {

    /**
     * 특정 사용자/종목의 최신 판단 N개 조회
     */
    List<TradeSignalEntity> findByUserIdAndCompanyIdOrderByCreatedAtDesc(
            Long userId,
            String companyId,
            Pageable pageable
    );

    /**
     * 최근 일정 시간(쿨타임) 내 동일 decisionType 발생 여부 (중복 방지용)
     */
    boolean existsByUserIdAndCompanyIdAndDecisionTypeAndCreatedAtAfter(
            Long userId,
            String companyId,
            TradeDecisionType decisionType,
            LocalDateTime after
    );

    /**
     * 특정 사용자/종목의 마지막 판단 1개 조회 (필요 시 사용)
     */
    @Query("""
            select ts
            from TradeSignalEntity ts
            where ts.userId = :userId
              and ts.companyId = :companyId
            order by ts.createdAt desc
           """)
    List<TradeSignalEntity> findLatestOne(Long userId, String companyId, Pageable pageable);

    /**
     * 특정 사용자 전체의 최신 판단 N개 조회 (마이페이지/로그용)
     */
    List<TradeSignalEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
