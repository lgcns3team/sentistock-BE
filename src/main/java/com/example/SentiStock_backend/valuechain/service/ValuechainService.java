package com.example.SentiStock_backend.valuechain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SentiStock_backend.valuechain.domain.dto.ValuechainResponseDto;
import com.example.SentiStock_backend.valuechain.domain.entity.ValuechainEntity;
import com.example.SentiStock_backend.valuechain.repository.ValuechainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValuechainService {
    private final ValuechainRepository valuechainRepository;

    public List<ValuechainResponseDto> getValuechainsByFromCompanyId(String companyId) {
        List<ValuechainEntity> entities = valuechainRepository.findByFromCompany_Id(companyId);

        return entities.stream()
                .map(ValuechainResponseDto::from)
                .toList();
    }
}
