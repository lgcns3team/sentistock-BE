package com.example.SentiStock_backend.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;   
                                        

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRequestDto {
    private String fcmToken;
}
