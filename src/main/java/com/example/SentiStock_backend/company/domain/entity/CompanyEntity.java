package com.example.SentiStock_backend.company.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Companies") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {
    @Id
    @Column(length = 10)
    private String id; 

    @Column(length = 50)
    private String name;

    private Long sectorId; 
}
