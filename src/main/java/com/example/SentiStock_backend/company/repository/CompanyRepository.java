package com.example.SentiStock_backend.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {

    // sector_id 기준으로 종목 리스트 조회
    List<CompanyEntity> findBySectorId(Long sectorId);
}