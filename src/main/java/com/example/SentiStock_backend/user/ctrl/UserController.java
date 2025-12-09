package com.example.SentiStock_backend.user.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.domain.dto.UserUpdateRequestDto;
import com.example.SentiStock_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserMeResponseDto getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String userId = userDetails.getUserId();
        return userService.getMyInfo(userId);
    }
    // 회원정보 수정 (닉네임 + 비밀번호)
    @PatchMapping("/me")
    public UserMeResponseDto updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateRequestDto request
    ) {
        String userId = userDetails.getUserId();
        return userService.updateMyInfo(userId, request);
    }
}
