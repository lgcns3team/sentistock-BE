package com.example.SentiStock_backend.sector.repository;

import com.example.SentiStock_backend.sector.domain.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<SectorEntity, Long> {
}
