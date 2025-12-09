package com.example.SentiStock_backend.favorite.domain.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCompanyId implements Serializable {
    private Long user;      
    private String company;  
}