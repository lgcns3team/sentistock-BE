package com.example.SentiStock_backend.auth.service;

import com.example.SentiStock_backend.auth.domain.RefreshToken;
import com.example.SentiStock_backend.auth.domain.dto.LoginRequestDto;
import com.example.SentiStock_backend.auth.domain.dto.LoginResponseDto;
import com.example.SentiStock_backend.auth.domain.dto.SignUpRequestDto;
import com.example.SentiStock_backend.auth.domain.dto.TokenReissueRequestDto;
import com.example.SentiStock_backend.auth.domain.dto.TokenResponseDto;
import com.example.SentiStock_backend.auth.jwt.JwtTokenProvider;
import com.example.SentiStock_backend.auth.repository.RefreshTokenRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
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
            .provider("LOCAL")        
            .providerId(null)         
            .build();

        userRepository.save(user);
    }

    // 점수 → 투자성향 매핑
    private String convertScoreToInvestorType(int score) {
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

        // 응답 DTO
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .investorType(user.getInvestorType())
                .subscribe(user.isSubscribe())
                .build();
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

        // 9) 응답 DTO 리턴
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
