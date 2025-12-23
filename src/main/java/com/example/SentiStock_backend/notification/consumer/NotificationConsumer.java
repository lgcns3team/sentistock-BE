package com.example.SentiStock_backend.notification.consumer;

import com.example.SentiStock_backend.event.StockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    @KafkaListener(
        topics = "stock-events",
        groupId = "notification-group"
    )
    public void consume(StockEvent event) {
        log.info("📥 Kafka Event Received: {}", event);
    }
}
