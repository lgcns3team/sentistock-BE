package com.example.SentiStock_backend.sentiment.repository;

import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StocksScoreRepository extends JpaRepository<StocksScoreEntity, Long> {

    /**
     * 특정 종목(companyId)의 감정 점수 기록 중
     * 최신 7개 데이터를 날짜 기준 내림차순으로 조회
     * (차트용 감정 점수 히스토리 조회)
     */
    List<StocksScoreEntity> findTop28ByCompanyIdOrderByDateDesc(String companyId);

    Optional<StocksScoreEntity> findTopByCompany_IdOrderByDateDesc(String companyId);
    
}
