package com.example.SentiStock_backend.notification.ctrl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.event.StockEvent;
import com.example.SentiStock_backend.event.StockEventProducer;
import com.example.SentiStock_backend.event.StockEventType;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class KafkaTestCtrl {

    private final StockEventProducer producer;

    @GetMapping("/kafka")
    public String sendTestEvent() {

        producer.publish(
                StockEvent.builder()
                        .userId(1L)
                        .type(StockEventType.SENTIMENT_CHANGED)
                        .companyId("005930")
                        .sentimentScore(0.42)
                        .sentimentChange(-0.35)
                        .occurredAt(LocalDateTime.now())
                        .build());

        return "Kafka event sent";
    }
}
