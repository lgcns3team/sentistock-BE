package com.example.SentiStock_backend.user.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.user.domain.dto.UserMeResponseDto;
import com.example.SentiStock_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}
