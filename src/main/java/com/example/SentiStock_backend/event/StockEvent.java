package com.example.SentiStock_backend.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString 
public class StockEvent {

    private StockEventType type;
    private String companyId;

    // 주가 관련
    private Double profitRate;

    // 감정 관련
    private Double sentimentScore;
    private Double sentimentChange;

    private LocalDateTime occurredAt;
}
