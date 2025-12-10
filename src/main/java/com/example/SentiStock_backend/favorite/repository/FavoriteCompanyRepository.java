package com.example.SentiStock_backend.favorite.repository;

import com.example.SentiStock_backend.favorite.domain.entity.FavoriteCompanyEntity;
import com.example.SentiStock_backend.favorite.domain.entity.FavoriteCompanyId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCompanyRepository
        extends JpaRepository<FavoriteCompanyEntity, FavoriteCompanyId> {

    boolean existsByUserIdAndCompanyId(Long userId, String companyId);

    Optional<FavoriteCompanyEntity> findByUserIdAndCompanyId(Long userId, String companyId);

    List<FavoriteCompanyEntity> findAllByUserId(Long userId);
}
