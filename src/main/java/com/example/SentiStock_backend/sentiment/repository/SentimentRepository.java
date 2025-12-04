package com.example.SentiStock_backend.sentiment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;

@Repository
public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {

     /**
     * 특정 종목(companyId)의 모든 감정 데이터 조회 (최신 순)
     */
    List<SentimentEntity> findByCompanyId(String companyId);

    /**
     * 특정 종목의 최근 N개 감정 데이터를 가져오기 위한 메서드
     * (서비스에서 .stream().limit(3) 또는 Pageable로 처리)
     */
    List<SentimentEntity> findTop3ByCompanyId(String companyId);

    /**
     * 특정 날짜 이후의 종목 감정 데이터 조회 (히스토리 그래프용)
     */
    List<SentimentEntity> findByCompanyIdAfterDate(
            String companyId,
            java.time.LocalDateTime after
    );
} 
    

