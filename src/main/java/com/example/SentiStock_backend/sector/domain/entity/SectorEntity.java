package com.example.SentiStock_backend.sector.domain.entity;

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
@Table(name = "Sectors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;   

    @Column(name = "name", length = 10, nullable = false)
    private String name;
}

