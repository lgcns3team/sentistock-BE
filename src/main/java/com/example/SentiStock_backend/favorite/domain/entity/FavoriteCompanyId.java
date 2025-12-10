package com.example.SentiStock_backend.favorite.domain.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FavoriteCompanyId implements Serializable {

    private Long userId;  
    private String companyId; 
}
