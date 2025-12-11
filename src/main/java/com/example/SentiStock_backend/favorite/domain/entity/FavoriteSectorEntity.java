package com.example.SentiStock_backend.favorite.domain.entity;

import com.example.SentiStock_backend.sector.domain.entity.SectorEntity;
import com.example.SentiStock_backend.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "Favorites_sectors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(FavoriteSectorId.class)
public class FavoriteSectorEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "sector_id", nullable = false)
    private Long sectorId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sector_id", insertable = false, updatable = false)
    private SectorEntity sector;

    public static FavoriteSectorEntity of(UserEntity user, SectorEntity sector) {
        return FavoriteSectorEntity.builder()
                .userId(user.getId())
                .sectorId(sector.getId())
                .user(user)
                .sector(sector)
                .build();
    }
}
