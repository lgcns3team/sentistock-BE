package com.example.SentiStock_backend.sentiment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @Column(nullable = false)
    private Double score; // 감정 점수 평균

    @Column(nullable = false)
    private LocalDateTime date; // 점수 생성 시간
}
