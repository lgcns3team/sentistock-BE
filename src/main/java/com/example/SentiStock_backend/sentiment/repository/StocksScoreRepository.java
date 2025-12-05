package com.example.SentiStock_backend.sentiment.repository;

import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StocksScoreRepository extends JpaRepository<StocksScoreEntity, Long> {

    List<StocksScoreEntity> findTop7ByCompanyIdOrderByDateDesc(String companyId);

}
