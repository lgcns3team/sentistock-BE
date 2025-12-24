package com.example.SentiStock_backend.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SentiStock_backend.company.domain.dto.CompanyDetailDto;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;


@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StockRepository stockRepository;

    public CompanyDetailDto getSnapshot(String companyId) {
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 종목코드: " + companyId));

        StockEntity latest = stockRepository.findTop1ByCompanyIdOrderByDateDesc(companyId)
                .orElseThrow(() -> new IllegalArgumentException("주가 데이터 없음: " + companyId));

        Long current = latest.getStckPrpr(); 
        Long prevClose = latest.getStckPrdyClpr(); 

        Double changeRate = null;
        if (prevClose != null && prevClose != 0 && current != null) {
            changeRate = (current - prevClose) * 100.0 / prevClose;
            changeRate = Math.round(changeRate * 100.0) / 100.0;
        }

        return CompanyDetailDto.builder()
                .companyId(companyId)
                .name(company.getName())
                .currentPrice(current)
                .changeRate(changeRate)
                .build();
    }

}
