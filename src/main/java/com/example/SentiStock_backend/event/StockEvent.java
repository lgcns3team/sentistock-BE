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

    private Long userId; 

    private StockEventType type;
    private String companyId;

    private Double profitRate;

    private Double sentimentScore;
    private Double sentimentChange;

    private LocalDateTime occurredAt;
}
