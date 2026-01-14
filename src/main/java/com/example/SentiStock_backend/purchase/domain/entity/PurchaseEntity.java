package com.example.SentiStock_backend.purchase.domain.entity;

import java.time.LocalDateTime;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Purchase", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "company_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @Column(name = "avg_price", nullable = false)
    private Float avgPrice;

    @Column(name = "pur_senti", nullable = false)
    private Double purSenti;

    @Column(name = "last_profit_event_rate")
    private Double lastProfitEventRate;

    @Column(name = "last_profit_event_at")
    private LocalDateTime lastProfitEventAt;

    @Column(name = "last_senti_event_score")
    private Double lastSentiEventScore;

    @Column(name = "last_senti_event_at")
    private LocalDateTime lastSentiEventAt;

    @Column(name = "last_senti_score")
    private Double lastSentiScore;

    @Column(name = "last_volume_event_rate")
    private Double lastVolumeEventRate;

    @Column(name = "last_sell_rule_code", length = 30)
    private String lastSellRuleCode;

    @Column(name = "last_signal_at")
    private LocalDateTime lastSignalAt;
}
