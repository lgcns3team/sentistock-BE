package com.example.SentiStock_backend.user.service;

import com.example.SentiStock_backend.auth.service.AuthService;
import com.example.SentiStock_backend.favorite.repository.FavoriteSectorRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.domain.dto.OnboardingRequestDto;
import com.example.SentiStock_backend.user.domain.dto.PasswordChangeRequestDto;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.SentiStock_backend.favorite.repository.FavoriteCompanyRepository;   
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;        
import com.example.SentiStock_backend.auth.repository.RefreshTokenRepository;  

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteSectorRepository favoriteSectorRepository;
    private final AuthService authService;
    private final FavoriteCompanyRepository favoriteCompanyRepository;
    private final PurchaseRepository purchaseRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    //  내 정보 조회
    @Transactional(readOnly = true)
    public UserMeResponseDto getMyInfo(String userId) {

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return UserMeResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .userEmail(user.getUserEmail())
                .passwordMasked("********")
                .provider(user.getProvider())  // 로컬/카카오 구분용
                .subscribe(user.isSubscribe())
                .investorType(user.getInvestorType())
                .build();
    }

 
    //  닉네임 변경 (회원정보 수정 탭)
    @Transactional
    public UserMeResponseDto updateMyNickname(String userId, String nickname) {

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (nickname != null && !nickname.isBlank()) {
            user.changeNickname(nickname);
        }

        return UserMeResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .userEmail(user.getUserEmail())
                .passwordMasked("********")
                .provider(user.getProvider())
                .subscribe(user.isSubscribe())
                .investorType(user.getInvestorType())
                .build();
    }


    //  비밀번호 변경 (계정 보안 탭, 로컬 유저만)
    @Transactional
    public void changePassword(String userId, PasswordChangeRequestDto dto) {

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 카카오 회원이면 비밀번호 변경 불가 (백엔드 방어 로직)
        if (user.isSocialUser()) {
            throw new IllegalStateException("카카오 회원은 비밀번호를 변경할 수 없습니다.");
        }

        // 현재 비밀번호 검증
        if (user.getUserPw() == null ||
            !passwordEncoder.matches(dto.getCurrentPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 & 확인 일치 
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 이전 비밀번호와 같은지 체크
        if (passwordEncoder.matches(dto.getNewPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("이전 비밀번호와 다른 비밀번호를 사용해주세요.");
        }

        // 5) 암호화 후 저장
        String encoded = passwordEncoder.encode(dto.getNewPassword());
        user.changePassword(encoded);
    }

    //  설문, 섹터선택 미실시자 다시 완료 처리
    @Transactional
    public void completeOnboarding(Long userId, OnboardingRequestDto request) {
        // 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        // 설문 점수 → 투자성향
        String investorType = authService.convertScoreToInvestorType(request.getInvestorScore());
        user.setInvestorType(investorType);
        // 기존 관심 섹터 모두 삭제
        favoriteSectorRepository.deleteAllByUserId(userId);
        // 관심 섹터 저장은 AuthService의 공통 메서드 재사용
        authService.saveFavoriteSectorsForUser(user, request.getFavoriteSectorIds());
    }

    //  회원탈퇴 (연관 데이터 + 토큰 + 유저 삭제)
    @Transactional
    public void deleteMyAccount(String userId) {

        // user_id로 유저 조회
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Long userPk = user.getId(); 

        // 즐겨찾기 섹터 삭제
        favoriteSectorRepository.deleteAllByUserId(userPk);

        // 즐겨찾기 종목 삭제
        favoriteCompanyRepository.deleteAllByUserId(userPk);

        // 구매 내역 삭제
        purchaseRepository.deleteAllByUser_Id(userPk);

        // 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUser(user);

        // 유저 삭제
        userRepository.delete(user);
    }


}
