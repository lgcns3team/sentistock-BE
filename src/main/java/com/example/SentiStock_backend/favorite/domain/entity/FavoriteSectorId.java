package com.example.SentiStock_backend.favorite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSectorId {
    private Long user;    
    private Long sector; 
}
