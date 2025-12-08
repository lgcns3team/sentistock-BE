package com.example.SentiStock_backend.valuechain.domain.entity;

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
@Table(name = "Valuechains")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValuechainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_company_id", nullable = false)
    private CompanyEntity toCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_company_id", nullable = false)
    private CompanyEntity fromCompany;

    @Column(nullable = false, length = 50)
    private String relationship; 
}
