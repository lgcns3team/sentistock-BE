package com.example.SentiStock_backend.sentiment.repository;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {

    /**
     * 뉴스 ID 리스트로 감정 데이터 조회 (최신 순)
     */
    List<SentimentEntity> findByNewsIdInOrderByDateDesc(List<Long> newsIds);

    /**
     * 특정 뉴스 ID에 대한 데이터 조회
     */
    List<SentimentEntity> findByNewsId(Long newsId);

    /**
     * 특정 뉴스 ID에 대한 최신 감정 데이터 1개 조회
     * (최신 뉴스와 감정 점수 매칭용)
     */
    Optional<SentimentEntity> findTopByNewsIdOrderByDateDesc(Long newsId);
   
}
