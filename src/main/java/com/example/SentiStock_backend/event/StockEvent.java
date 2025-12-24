package com.example.SentiStock_backend.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEvent {

    private Long userId;
    private String companyId;
    private Double profitRate;
    private Double sentimentChange;

}
