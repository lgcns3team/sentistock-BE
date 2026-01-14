package com.example.SentiStock_backend.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        String credPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        if (credPath == null || credPath.isBlank()) {
            log.warn("GOOGLE_APPLICATION_CREDENTIALS is not set. Firebase will not be initialized.");
            return;
        }

        try (InputStream serviceAccount = new FileInputStream(credPath)) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp initialized using GOOGLE_APPLICATION_CREDENTIALS: {}", credPath);
            } else {
                log.info("FirebaseApp already initialized (apps={})", FirebaseApp.getApps().size());
            }

        } catch (Exception e) {
            log.error("Failed to initialize FirebaseApp from GOOGLE_APPLICATION_CREDENTIALS: {}", credPath, e);
        }
    }
}
