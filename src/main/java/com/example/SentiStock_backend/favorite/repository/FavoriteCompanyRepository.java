package com.example.SentiStock_backend.favorite.repository;

import com.example.SentiStock_backend.favorite.domain.entity.FavoriteCompanyEntity;
import com.example.SentiStock_backend.favorite.domain.entity.FavoriteCompanyId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteCompanyRepository
        extends JpaRepository<FavoriteCompanyEntity, FavoriteCompanyId> {

    boolean existsByUserIdAndCompanyId(Long userId, String companyId);

    Optional<FavoriteCompanyEntity> findByUserIdAndCompanyId(Long userId, String companyId);

    List<FavoriteCompanyEntity> findAllByUserId(Long userId);

    @Query("""
        select fc
        from FavoriteCompanyEntity fc
        join fetch fc.company c
        where fc.userId = :userId
        """)
    List<FavoriteCompanyEntity> findFavoriteCompaniesByUserId(@Param("userId") Long userId);

    int countByUserId(Long userId);
    
    void deleteAllByUserId(Long userId);
}
