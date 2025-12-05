package com.example.SentiStock_backend.news.repository;

import com.example.SentiStock_backend.news.domain.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    List<NewsEntity> findByCompanyId(Long companyId);
}
