package com.example.SentiStock_backend.purchase.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseResponseDto {
    private Long id;
    private String companyId;
    private Float avgPrice;
}
