package com.example.SentiStock_backend.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FirebaseService {

    public void sendPush(String token, String title, String body) {
        log.info("[FCM] sendPush called token={}", token);

        Message message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("[FCM] send success messageId={}", response);
        } catch (FirebaseMessagingException e) {
            log.error("[FCM] send failed", e);
        }
    }
}
