package com.example.SentiStock_backend.purchase.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequestDTO {
    private Long userId;
    private String companyId; 
    private Float avgPrice;
}
