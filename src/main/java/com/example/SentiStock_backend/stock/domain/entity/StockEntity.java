package com.example.SentiStock_backend.stock.domain.entity;

import java.time.LocalDateTime;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;

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

@Entity
@Table(name = "Stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date; 

    @Column(name = "stck_prpr", nullable = false)
    private Long stckPrpr;

    @Column(name = "stck_oprc", nullable = false)
    private Long stckOprc;

    @Column(name = "stck_hgpr", nullable = false)
    private Long stckHgpr;

    @Column(name = "stck_lwpr", nullable = false)
    private Long stckLwpr;

    @Column(name = "acml_vol", nullable = false)
    private Long acmlVol;
    
    @Column(name = "stck_prdy_clpr", nullable = false)
    private Long stckPrdyClpr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;
}