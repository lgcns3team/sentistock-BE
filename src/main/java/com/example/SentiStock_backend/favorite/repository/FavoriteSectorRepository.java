package com.example.SentiStock_backend.favorite.repository;

import com.example.SentiStock_backend.favorite.domain.entity.FavoriteSectorEntity;
import com.example.SentiStock_backend.favorite.domain.entity.FavoriteSectorId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteSectorRepository extends JpaRepository<FavoriteSectorEntity, FavoriteSectorId> {

    List<FavoriteSectorEntity> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    boolean existsByUserIdAndSectorId(Long userId, Long sectorId);

    boolean existsByUserId(Long userId);

}
