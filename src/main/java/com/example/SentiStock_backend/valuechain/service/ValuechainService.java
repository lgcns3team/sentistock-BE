package com.example.SentiStock_backend.valuechain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SentiStock_backend.stock.domain.dto.StockChangeInfo;
import com.example.SentiStock_backend.stock.service.StockService;
import com.example.SentiStock_backend.valuechain.domain.dto.ValuechainResponseDto;
import com.example.SentiStock_backend.valuechain.domain.entity.ValuechainEntity;
import com.example.SentiStock_backend.valuechain.repository.ValuechainRepository;


@Service
@Transactional(readOnly = true)
public class ValuechainService {
    
    @Autowired
    private ValuechainRepository valuechainRepository;

    @Autowired
    private StockService stockService;

    public List<ValuechainResponseDto> getValuechainsByFromCompanyId(String companyId) {

        List<ValuechainEntity> chains =
                valuechainRepository.findByFromCompany_Id(companyId);

        return chains.stream()
                .map(vc -> {
                    String toCompanyId = vc.getToCompany().getId();
                    String toCompanyName = vc.getToCompany().getName();

                    
                    StockChangeInfo info = stockService.getLatestPriceAndChange(toCompanyId);
                    Long currentPrice = null;
                    Double changeRate = null;

                    if (info != null) {
                        currentPrice = info.getCurrentPrice();
                        changeRate = info.getChangeRate();
                    }

                    return ValuechainResponseDto.builder()
                            .toCompanyId(toCompanyId)
                            .toCompanyName(toCompanyName)
                            .relationship(vc.getRelationship())
                            .currentPrice(currentPrice)
                            .changeRate(changeRate)
                            .build();
                })
                .toList();
    }
}
