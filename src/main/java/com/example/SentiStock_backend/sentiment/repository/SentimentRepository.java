package com.example.SentiStock_backend.sentiment.repository;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {

    /**
     * 뉴스 ID 리스트로 감정 데이터 조회 (최신 순)
     */
    List<SentimentEntity> findByNewsIdInOrderByDateDesc(List<String> newsIds);

    /**
     * 특정 뉴스 ID에 대한 데이터 조회
     */
    List<SentimentEntity> findByNewsId(String newsId);
}
