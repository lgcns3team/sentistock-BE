package com.example.SentiStock_backend.auth.service;

import com.example.SentiStock_backend.auth.domain.RefreshToken;
import com.example.SentiStock_backend.auth.domain.dto.LoginRequestDto;
import com.example.SentiStock_backend.auth.domain.dto.LoginResponseDto;
import com.example.SentiStock_backend.auth.domain.dto.SignUpRequestDto;
import com.example.SentiStock_backend.auth.domain.dto.TokenReissueRequestDto;
import com.example.SentiStock_backend.auth.domain.dto.TokenResponseDto;
import com.example.SentiStock_backend.auth.jwt.JwtTokenProvider;
import com.example.SentiStock_backend.auth.oauth.dto.KakaoTokenResponse;
import com.example.SentiStock_backend.auth.oauth.dto.KakaoUserInfoResponse;
import com.example.SentiStock_backend.auth.oauth.service.KakaoOAuthService;
import com.example.SentiStock_backend.auth.repository.RefreshTokenRepository;
import com.example.SentiStock_backend.favorite.domain.entity.FavoriteSectorEntity;
import com.example.SentiStock_backend.favorite.repository.FavoriteSectorRepository;
import com.example.SentiStock_backend.sector.domain.entity.SectorEntity;
import com.example.SentiStock_backend.sector.repository.SectorRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoOAuthService kakaoOAuthService;
    private final SectorRepository sectorRepository;
    private final FavoriteSectorRepository favoriteSectorRepository;

    // 회원가입 (로컬)
    @Transactional
    public void signup(SignUpRequestDto request) {
        // 아이디 중복 체크
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 & 비밀번호 확인 일치 여부
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(request.getPassword());

        // 투자성향 점수 → 투자성향 타입 변환
        String investorType = convertScoreToInvestorType(request.getInvestorScore());

        // UserEntity 생성
        UserEntity user = UserEntity.builder()
                .nickname(request.getNickname())
                .userId(request.getUserId())
                .userPw(encodedPw)
                .userEmail(request.getUserEmail())
                .investorType(investorType)
                .isSubscribe(false)
                .build();

        // 사용자 저장
        userRepository.save(user);

        // 관심 섹터 저장 (필수)
        saveFavoriteSectorsForUser(user, request.getFavoriteSectorIds());
    }

    // 점수 → 투자성향 매핑 (UserService에서도 재사용할 수 있게 public으로 변경)
    public String convertScoreToInvestorType(int score) {
        if (score >= 30) {
            return "공격투자형";
        } else if (score >= 25) {
            return "적극투자형";
        } else if (score >= 20) {
            return "위험중립형";
        } else if (score >= 15) {
            return "안전추구형";
        } else {
            return "안정형";
        }
    }

    // 관심 섹터 저장 (UserService에서도 재사용할 수 있게 public으로 변경)
    public void saveFavoriteSectorsForUser(UserEntity user, List<Long> favoriteSectorIds) {

        if (favoriteSectorIds == null || favoriteSectorIds.isEmpty()) {
            throw new IllegalArgumentException("관심 섹터를 정확히 5개 선택해야 합니다.");
        }

        // 중복 제거 후 개수 체크
        List<Long> distinctIds = favoriteSectorIds.stream()
                .distinct()
                .toList();

        if (distinctIds.size() != 5) {
            throw new IllegalArgumentException("관심 섹터는 중복 없이 정확히 5개 선택해야 합니다.");
        }

        distinctIds.forEach(sectorId -> {
            SectorEntity sector = sectorRepository.findById(sectorId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("존재하지 않는 섹터입니다. id=" + sectorId));

            FavoriteSectorEntity entity = FavoriteSectorEntity.of(user, sector);
            favoriteSectorRepository.save(entity);
        });
    }

    // 로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        // 유저 조회
        UserEntity user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken tokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(
                        Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenValidityInMs())
                )
                .revoked(false)
                .build();

        refreshTokenRepository.save(tokenEntity);

        // 온보딩 필요 여부 계산
        boolean hasFavoriteSectors = favoriteSectorRepository.existsByUserId(user.getId());
        boolean onboardingRequired =
                "없음".equals(user.getInvestorType()) || !hasFavoriteSectors;

        // 응답 DTO
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .investorType(user.getInvestorType())
                .subscribe(user.isSubscribe())
                // 응답에 온보딩 플래그 포함
                .onboardingRequired(onboardingRequired)
                .build();
    }

    // 카카오 OAuth 로그인
    @Transactional
    public LoginResponseDto kakaoLogin(String code) {

        KakaoTokenResponse tokenResponse = kakaoOAuthService.getKakaoToken(code);
        KakaoUserInfoResponse userInfo =
                kakaoOAuthService.getUserInfo(tokenResponse.getAccess_token());

        Long kakaoId = userInfo.getId();
        String email = userInfo.getKakao_account() != null
                ? userInfo.getKakao_account().getEmail()
                : null;
        String nickname = (userInfo.getKakao_account() != null
                && userInfo.getKakao_account().getProfile() != null)
                ? userInfo.getKakao_account().getProfile().getNickname()
                : null;

        String provider = "KAKAO";
        String providerId = String.valueOf(kakaoId);

        UserEntity user = userRepository
                .findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> createKakaoUser(provider, providerId, email, nickname));

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken tokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(
                        Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenValidityInMs())
                )
                .revoked(false)
                .build();

        refreshTokenRepository.save(tokenEntity);

        // 카카오 로그인도 온보딩 여부 계산
        boolean hasFavoriteSectors = favoriteSectorRepository.existsByUserId(user.getId());
        boolean onboardingRequired =
                "없음".equals(user.getInvestorType()) || !hasFavoriteSectors;

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .investorType(user.getInvestorType())
                .subscribe(user.isSubscribe())
                // 응답에 온보딩 플래그 포함
                .onboardingRequired(onboardingRequired)
                .build();
    }

    // 최초 카카오 로그인 시 UserEntity 생성
    private UserEntity createKakaoUser(String provider,
                                       String providerId,
                                       String email,
                                       String nickname) {

        // user_id는 고유하게 "kakao_{카카오ID}" 형식으로 생성
        String userId = "kakao_" + providerId;

        // user_pw를 채우기 위한 랜덤 비밀번호
        String randomPw = UUID.randomUUID().toString();
        String encodedPw = passwordEncoder.encode(randomPw);

        // 카카오 기본 투자성향을 "없음"으로 설정
        String defaultInvestorType = "없음";

        UserEntity user = UserEntity.builder()
                .nickname(nickname != null ? nickname : "카카오유저_" + providerId)
                .userId(userId)
                .userPw(encodedPw)
                .userEmail(email != null ? email : (userId + "@kakao.local"))
                .investorType(defaultInvestorType)
                .isSubscribe(false)
                .provider(provider)
                .providerId(providerId)
                .build();

        return userRepository.save(user);
    }

    // 토큰 재발급
    @Transactional
    public TokenResponseDto reissue(TokenReissueRequestDto request) {
        String refreshToken = request.getRefreshToken();

        // Refresh Token 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }
        // 토큰이 Refresh Token인지 확인
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 아닙니다.");
        }
        // DB에서 RefreshToken 엔티티 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("저장된 Refresh Token이 없습니다. 다시 로그인해주세요."));

        // 이미 만료되었거나 사용 중지된 토큰인지 확인
        if (storedToken.isRevoked() || storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new IllegalArgumentException("만료되었거나 사용 중지된 Refresh Token 입니다. 다시 로그인해주세요.");
        }
        // 토큰에서 userId 추출, 유저 일치 여부 확인
        String userIdFromToken = jwtTokenProvider.getUserId(refreshToken);
        UserEntity user = storedToken.getUser();

        if (!user.getUserId().equals(userIdFromToken)) {
            throw new IllegalArgumentException("토큰 정보와 사용자 정보가 일치하지 않습니다.");
        }

        // 새 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(userIdFromToken);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userIdFromToken);

        // 기존 RefreshToken 엔티티는 revoke
        storedToken.revoke();

        // 새 RefreshToken 엔티티 저장
        RefreshToken newTokenEntity = RefreshToken.builder()
                .user(user)
                .token(newRefreshToken)
                .expiresAt(
                        Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenValidityInMs())
                )
                .revoked(false)
                .build();

        refreshTokenRepository.save(newTokenEntity);

        // 응답 DTO 리턴
        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    // 로그아웃
    @Transactional
    public void logout(String userId) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 유저의 모든 RefreshToken 제거
        refreshTokenRepository.deleteByUser(user);
    }
}
