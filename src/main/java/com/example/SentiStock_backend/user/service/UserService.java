package com.example.SentiStock_backend.user.service;

import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserMeResponseDto getMyInfo(String userId) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return UserMeResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .userEmail(user.getUserEmail())
                .investorType(user.getInvestorType())
                .subscribe(user.isSubscribe())
                .build();
    }
}
