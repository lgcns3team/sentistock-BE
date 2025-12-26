package com.example.SentiStock_backend.event;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEvent {

    private Long userId;
    private String companyId;
    private StockEventType type;
    private Double profitRate;
    private Double sentimentChange;
    private LocalDateTime occurredAt;
    private Double currentSentiment;


}
