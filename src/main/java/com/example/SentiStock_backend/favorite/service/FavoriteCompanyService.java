package com.example.SentiStock_backend.favorite.service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.favorite.domain.dto.FavoriteCompanyResponseDto;
import com.example.SentiStock_backend.favorite.domain.entity.FavoriteCompanyEntity;
import com.example.SentiStock_backend.favorite.repository.FavoriteCompanyRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteCompanyService {

    private final FavoriteCompanyRepository favoriteCompanyRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    
    //현재 즐겨찾기 여부 조회 
    @Transactional(readOnly = true)
    public boolean isFavorite(Long userId, String companyId) {
        return favoriteCompanyRepository.existsByUserIdAndCompanyId(userId, companyId);
    }

    
    //즐겨찾기 설정
    @Transactional
    public boolean toggleFavorite(Long userId, String companyId) {

        // 이미 즐겨찾기인지 확인
        Optional<FavoriteCompanyEntity> existing =
                favoriteCompanyRepository.findByUserIdAndCompanyId(userId, companyId);

        if (existing.isPresent()) {
            // 있으면 해제 
            favoriteCompanyRepository.delete(existing.get());
            return false;
        }

        // 없으면 추가 
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("종목이 존재하지 않습니다."));

        FavoriteCompanyEntity favorite = FavoriteCompanyEntity.of(user, company);
        favoriteCompanyRepository.save(favorite);

        return true;
    }
    
    @Transactional(readOnly = true)
    public List<FavoriteCompanyResponseDto> getMyFavoriteCompanies(Long userId) {
        return favoriteCompanyRepository.findFavoriteCompaniesByUserId(userId);
    }

}
