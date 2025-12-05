package com.example.SentiStock_backend.sentiment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Stocks_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StocksScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", length = 10, nullable = false)
    private String companyId;

    @Column(nullable = false)
    private Double score; // 감정 점수 평균

    @Column(nullable = false)
    private LocalDateTime date; // 점수 생성 시간
}
