package com.example.SentiStock_backend.user.service;

import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;   

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInvestorService {

    private final UserRepository userRepository;

    public String getInvestorType(Long userId) {

        if (userId == null) {
            return "위험중립형"; 
        }

        return userRepository.findById(userId)
                .map(UserEntity::getInvestorType)
                .orElse("위험중립형");
    }
}
