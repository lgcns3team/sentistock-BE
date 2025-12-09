package com.example.SentiStock_backend.company.repository;


import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
        
}
