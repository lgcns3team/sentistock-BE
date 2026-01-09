package com.example.SentiStock_backend.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;


import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public void sendPush(String token, String title, String body) {

        Message message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("[FCM] send success messageId=" + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("[FCM] send failed");
            e.printStackTrace();
        }
    }
}