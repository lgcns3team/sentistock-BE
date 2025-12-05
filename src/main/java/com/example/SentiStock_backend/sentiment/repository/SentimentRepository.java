package com.example.SentiStock_backend.sentiment.repository;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {

    // 뉴스 1개에 대한 감정 데이터 조회
    List<SentimentEntity> findByNewsId(String newsId);

    // 뉴스 여러 개에 대한 감정 데이터 조회
    List<SentimentEntity> findByNewsIdIn(List<String> newsIds);

    // 최신 뉴스 감정 데이터 상위 3개 조회
    List<SentimentEntity> findTop3ByNewsIdInOrderByDateDesc(List<String> newsIds);

    // 특정 날짜 이후 데이터 조회 (히스토리)
    List<SentimentEntity> findByNewsIdInAndDateAfterOrderByDateAsc(
            List<String> newsIds,
            LocalDateTime after
    );
}
