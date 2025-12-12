package com.example.SentiStock_backend.user.service;

import com.example.SentiStock_backend.favorite.repository.FavoriteCompanyRepository;
import com.example.SentiStock_backend.user.domain.dto.SubscriptionInfoResponseDto;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final FavoriteCompanyRepository favoriteCompanyRepository;
    

    // 내 구독 정보 조회
    @Transactional(readOnly = true)
    public SubscriptionInfoResponseDto getMySubscription(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new SubscriptionInfoResponseDto(
                user.isSubscribe(),                  
                user.getSubscribeAt()     
        );
    }

    // 구독 시작
    @Transactional
    public SubscriptionInfoResponseDto startSubscription(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isSubscribe()) {
            // 이미 구독중이면 그냥 현재 상태 반환
            return new SubscriptionInfoResponseDto(
                    true,
                    user.getSubscribeAt()
            );
        }

        user.startSubscription(LocalDate.now());

        return new SubscriptionInfoResponseDto(
                true,
                user.getSubscribeAt()
        );
    }


    // 구독 해지
    @Transactional
    public SubscriptionInfoResponseDto cancelSubscription(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 이미 구독이 아닌 상태면 그냥 리턴
        if (!user.isSubscribe()) {
            return new SubscriptionInfoResponseDto(false, null);
        }

        // 즐겨찾기 개수 확인
        int currentCount = favoriteCompanyRepository.countByUserId(userId);

        // 5개 초과면 전부 삭제
        if (currentCount > 5) {
            favoriteCompanyRepository.deleteAllByUserId(userId);
        }

        // 구독 해지
        user.cancelSubscription();

        // 응답은 구독 해지 상태로 반환
        return new SubscriptionInfoResponseDto(false, null);
    }
}
