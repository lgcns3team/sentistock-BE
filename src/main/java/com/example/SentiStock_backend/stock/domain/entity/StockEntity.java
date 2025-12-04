package com.example.SentiStock_backend.stock.domain.entity;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "Stocks") // 실제 테이블명 맞게 수정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date; 

    private Long stckPrpr;  // 현재가
    private Long stckOprc;  // 시가
    private Long stckHgpr;  // 고가
    private Long stckLwpr;  // 저가
    private Long acmlVol;   // 누적 거래량
    private Long stckPrdyClpr; // 전일 종가

    @Column(length = 10)
    private String companyId;
}