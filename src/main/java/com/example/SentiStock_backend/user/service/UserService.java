package com.example.SentiStock_backend.user.service;

import com.example.SentiStock_backend.user.domain.UserEntity;
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
        System.out.println("[DEBUG] 닉네임 변경: " + dto.getNickname());
        user.changeNickname(dto.getNickname());
    }
    // 비밀번호 변경
    if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
        System.out.println("[DEBUG] 새 비밀번호 요청 들어옴, provider = " + user.getProvider());

        if (user.isLocalUser()) {
            System.out.println("[DEBUG] LOCAL 유저 비밀번호 변경 실행");
            String encoded = passwordEncoder.encode(dto.getNewPassword());
            user.changePassword(encoded);
        } else {
            throw new IllegalStateException("카카오 회원은 비밀번호 변경이 불가합니다.");
        }
    }

    return UserMeResponseDto.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .userEmail(user.getUserEmail())
            .passwordMasked("********")
            .build();
}

}
