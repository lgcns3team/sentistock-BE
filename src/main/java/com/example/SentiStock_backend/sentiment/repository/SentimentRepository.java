package com.example.SentiStock_backend.sentiment.repository;

import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {

    /**
     * 뉴스 ID 리스트로 감정 데이터 조회 (최신 순)
     */
    List<SentimentEntity> findTop20ByNewsIdInOrderByDateDesc(List<Long> newsIds);

    /**
     * 특정 뉴스 ID에 대한 데이터 조회
     */
    List<SentimentEntity> findByNewsId(Long newsId);

    /**
     * 특정 뉴스 ID에 대한 최신 감정 데이터 1개 조회
     * (최신 뉴스와 감정 점수 매칭용)
     */
    Optional<SentimentEntity> findTopByNewsIdOrderByDateDesc(Long newsId);

    /**
     * 종목별 감정 비율 집계 조회
     */
    @Query(value = """
                SELECT
                    SUM(CASE WHEN s.label = 'POS' THEN 1 ELSE 0 END) AS posCount,
                    SUM(CASE WHEN s.label = 'NEG' THEN 1 ELSE 0 END) AS negCount,
                    SUM(CASE WHEN s.label = 'NEU' THEN 1 ELSE 0 END) AS neuCount,
                    COUNT(*) AS totalCount
                FROM sentiments s
                JOIN news n ON s.news_id = n.id
                WHERE n.company_id = :companyId
            """, nativeQuery = true)
    List<Object[]> getSentimentCountByCompany(@Param("companyId") String companyId);

}
