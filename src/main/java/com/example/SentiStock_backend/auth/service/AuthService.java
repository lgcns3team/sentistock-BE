package com.example.SentiStock_backend.auth.service;

import com.example.SentiStock_backend.auth.domain.dto.*;
import com.example.SentiStock_backend.auth.jwt.JwtTokenProvider;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    
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

        // UserEntity 생성
        UserEntity user = UserEntity.builder()
                .nickname(request.getNickname())
                .userId(request.getUserId())
                .userPw(encodedPw)
                .userEmail(request.getUserEmail())
                .investorType(request.getInvestorType())
                .isSubscribe(request.isSubscribe())
                .refreshToken(null)   
                .build();

        userRepository.save(user);
    }

    // 로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        // 1) 유저 조회
        UserEntity user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 2) 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3) 토큰 생성 (JwtTokenProvider.createToken만 사용)
        String accessToken = jwtTokenProvider.createToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createToken(user.getUserId());

        // 4) Refresh Token DB에 저장
        user.updateRefreshToken(refreshToken);

        // 5) 응답 DTO 리턴
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

        // 토큰에서 userId 추출
        String userId = jwtTokenProvider.getUserId(refreshToken);

        // 유저 조회
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // DB에 저장된 RefreshToken과 동일한지 확인
        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("저장된 Refresh Token과 일치하지 않습니다.");
        }

        // 새 토큰 발급 
        String newAccessToken = jwtTokenProvider.createToken(userId);
        String newRefreshToken = jwtTokenProvider.createToken(userId);

        // DB에 Refresh Token 갱신
        user.updateRefreshToken(newRefreshToken);

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }
}
