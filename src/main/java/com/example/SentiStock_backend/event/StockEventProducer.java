package com.example.SentiStock_backend.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "stock-events";

    public void publish(StockEvent event) {
        kafkaTemplate.send(TOPIC, event.getCompanyId(), event);
    }
}
