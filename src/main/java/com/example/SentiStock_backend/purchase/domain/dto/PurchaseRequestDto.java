package com.example.SentiStock_backend.purchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequestDto {
    private String companyId; 
    private Float avgPrice;
}
