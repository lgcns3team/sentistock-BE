package com.example.SentiStock_backend.user.service;

import com.example.SentiStock_backend.auth.service.AuthService;
import com.example.SentiStock_backend.favorite.repository.FavoriteSectorRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.domain.dto.OnboardingRequestDto;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.domain.dto.UserUpdateRequestDto;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteSectorRepository favoriteSectorRepository;
    private final AuthService authService;

    // 회원정보 조회
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
                .build();
    }

    // 회원정보 수정
    @Transactional
    public UserMeResponseDto updateMyInfo(String userId, UserUpdateRequestDto dto) {

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 닉네임 변경
        if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
            user.changeNickname(dto.getNickname());
        }

        // 비밀번호 변경
        String newPw = dto.getNewPassword();
        String confirmPw = dto.getConfirmNewPassword();

        // 둘 다 값이 있을 때만 비밀번호 변경 시도
        if (newPw != null && !newPw.isBlank()) {

            // 카카오 유저 차단
            if (user.isSocialUser()) {
                throw new IllegalStateException("카카오 회원은 비밀번호 변경이 불가합니다.");
            }

            // 비밀번호 확인 체크
            if (confirmPw == null || !newPw.equals(confirmPw)) {
                throw new IllegalArgumentException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }

            String encoded = passwordEncoder.encode(newPw);
            user.changePassword(encoded);
        }

        return UserMeResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .userEmail(user.getUserEmail())
                .passwordMasked("********")
                .build();
    }

    // 설문, 섹터선택 미실시자 다시 완료 처리
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


}
