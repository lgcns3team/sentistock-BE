package com.example.SentiStock_backend.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.stock.domain.dto.StockHeatmapItemDto;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public List<StockHeatmapItemDto> getSectorHeatmap(Long sectorId) {

        // 1) 섹터에 속한 종목들 찾기
        List<CompanyEntity> companies = companyRepository.findBySectorId(sectorId);

        List<StockHeatmapItemDto> result = new ArrayList<>();

        // 2) 각 종목별로 최신 시세 한 건 조회
        for (CompanyEntity company : companies) {
            StockEntity latestStock = stockRepository
                    .findTopByCompanyIdOrderByDateDesc(company.getId())
                    .orElse(null);

            if (latestStock == null) {
                continue; // 아직 시세 데이터 없는 종목은 스킵
            }

            Long current = latestStock.getStckPrpr();
            Long prevClose = latestStock.getStckPrdyClpr();

            Double changeRate = null;
            if (current != null && prevClose != null && prevClose != 0) {
                long diff = current - prevClose;
                changeRate = diff * 100.0 / prevClose;
            }

            // 3) DTO로 매핑해서 리스트에 추가
            result.add(
                    StockHeatmapItemDto.builder()
                            .companyId(company.getId())
                            .companyName(company.getName())
                            .currentPrice(current)
                            .changeRate(changeRate)
                            .build());
        }

        return result;
    }
}
