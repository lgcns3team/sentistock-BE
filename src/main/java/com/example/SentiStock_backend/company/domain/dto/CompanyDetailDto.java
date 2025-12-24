package com.example.SentiStock_backend.company.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailDto {
    
    private String companyId;   
    private String name;        
    private Long currentPrice; 
    private Double changeRate;  
}
