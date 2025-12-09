package com.example.SentiStock_backend.valuechain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SentiStock_backend.valuechain.domain.entity.ValuechainEntity;

@Repository
public interface ValuechainRepository extends JpaRepository<ValuechainEntity, Long> {
    List<ValuechainEntity> findByFromCompany_Id(String companyId);
}
