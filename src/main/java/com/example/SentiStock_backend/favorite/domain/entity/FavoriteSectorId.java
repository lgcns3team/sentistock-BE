package com.example.SentiStock_backend.favorite.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FavoriteSectorId implements Serializable {

    private Long userId;
    private Long sectorId;
}
