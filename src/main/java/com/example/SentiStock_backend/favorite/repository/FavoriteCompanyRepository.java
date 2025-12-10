package com.example.SentiStock_backend.favorite.repository;

import com.example.SentiStock_backend.favorite.domain.dto.FavoriteCompanyResponseDto;
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
        select new com.example.SentiStock_backend.favorite.domain.dto.FavoriteCompanyResponseDto(
            fc.company.id,
            fc.company.name
        )
        from FavoriteCompanyEntity fc
        where fc.userId = :userId
        """)
    List<FavoriteCompanyResponseDto> findFavoriteCompaniesByUserId(@Param("userId") Long userId);

}
