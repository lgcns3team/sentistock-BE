package com.example.SentiStock_backend.valuechain.domain.dto;

import com.example.SentiStock_backend.valuechain.domain.entity.ValuechainEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValuechainResponseDto {

    private String toCompanyId;     
    private String toCompanyName;   

    private String relationship;

    public static ValuechainResponseDto from(ValuechainEntity entity) {
        return ValuechainResponseDto.builder()
                .toCompanyId(entity.getToCompany().getId())
                .toCompanyName(entity.getToCompany().getName())
                .relationship(entity.getRelationship())
                .build();
    }
}