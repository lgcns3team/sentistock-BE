package com.example.SentiStock_backend.favorite.domain.dto; 

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;   
import lombok.NoArgsConstructor;
import lombok.Setter;   

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteSectorResponseDto {
    private Long sectorId;
    private String sectorName;
}
