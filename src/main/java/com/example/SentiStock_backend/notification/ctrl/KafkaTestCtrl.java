package com.example.SentiStock_backend.notification.ctrl;

import com.example.SentiStock_backend.event.StockEvent;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaTestCtrl {

    private final KafkaTemplate<String, StockEvent> kafkaTemplate;

    @PostMapping("/test/kafka")
    public void sendTestEvent() {

        StockEvent event = StockEvent.builder()
                .userId(1L)
                .companyId("005930")
                .profitRate(0.0)
                .sentimentChange(30.0)
                .build();

        kafkaTemplate.send("stock-events", event);
    }

    @PostMapping("/test/stock-event")
    public void send(@RequestBody StockEvent event) {
        kafkaTemplate.send("stock-events", event);
    }

}
