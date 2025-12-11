package com.example.SentiStock_backend.favorite.service;

import java.util.List;
import com.example.SentiStock_backend.favorite.domain.dto.FavoriteSectorResponseDto;
import com.example.SentiStock_backend.favorite.domain.entity.FavoriteSectorEntity;  
import com.example.SentiStock_backend.favorite.repository.FavoriteSectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;  

@Service
@RequiredArgsConstructor
public class FavoriteSectorService {

    private final FavoriteSectorRepository favoriteSectorRepository;

    public List<FavoriteSectorResponseDto> getMyFavoriteSectors(Long userId) {

        List<FavoriteSectorEntity> favorites =
                favoriteSectorRepository.findAllByUserId(userId);

        return favorites.stream()
                .map(f -> FavoriteSectorResponseDto.builder()
                        .sectorId(f.getSectorId())
                        .sectorName(f.getSector().getName())  
                        .build())
                .toList();
    }
}
