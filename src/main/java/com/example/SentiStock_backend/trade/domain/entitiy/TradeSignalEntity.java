package com.example.SentiStock_backend.trade.domain.entitiy;

import java.time.LocalDateTime;

import com.example.SentiStock_backend.trade.domain.type.TradeDecisionType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TradeSignal")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeSignalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "company_id", nullable = false, length = 10)
    private String companyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type", nullable = false, length = 40)
    private TradeDecisionType decisionType;

    @Column(name = "sentiment_score")
    private Double sentimentScore;

    @Column(name = "sentiment_delta")
    private Double sentimentDelta;

    @Column(name = "volume_rate")
    private Double volumeRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
