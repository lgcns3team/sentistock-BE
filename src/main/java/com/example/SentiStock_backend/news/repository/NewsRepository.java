package com.example.SentiStock_backend.news.repository;

import com.example.SentiStock_backend.news.domain.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    /**
     * 특정 종목(companyId)의 뉴스 전체 조회
     */
    List<NewsEntity> findByCompanyId(String companyId);

    /**
     * 특정 종목(companyId)의 뉴스 중
     * 최신 순(날짜 기준 DESC)으로 정렬하여 조회
     * → 가장 최근 뉴스 우선 가져올 때 사용
     */
    List<NewsEntity> findByCompanyIdOrderByDateDesc(String companyId);

}
