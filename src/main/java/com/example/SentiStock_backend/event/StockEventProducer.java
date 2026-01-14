package com.example.SentiStock_backend.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventProducer {

    private final KafkaTemplate<String, StockEvent> kafkaTemplate;

    private static final String TOPIC = "stock-events";

    public void publish(StockEvent event) {
        log.info(" PRODUCING EVENT: {}", event);

        kafkaTemplate.send(TOPIC, event.getCompanyId(), event);
        log.info("sending event = {}", event);

    }

}
