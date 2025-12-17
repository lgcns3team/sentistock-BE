package com.example.SentiStock_backend.notification.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.notification.service.FirebaseService;

@RestController
@RequestMapping("/test")
public class FirebaseCtrl {

    private final FirebaseService firebaseService;

    public FirebaseCtrl(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/fcm")
    public String testFcm(
            @RequestParam String token,
            @RequestParam(defaultValue = "📊 감정 알림") String title,
            @RequestParam(defaultValue = "FCM 테스트 메시지") String body) {
        firebaseService.sendPush(token, title, body);
        return "sent";
    }

}
