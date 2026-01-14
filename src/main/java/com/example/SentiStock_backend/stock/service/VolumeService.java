package com.example.SentiStock_backend.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VolumeService {

    private final StockRepository stockRepository;

    /**
     * 최근 4일 평균 대비 오늘 거래량 변화율(%)
     */
    public double calculateVolumeRate(String companyId) {

        List<StockEntity> stocks =
                stockRepository.findTop5ByCompanyIdOrderByDateDesc(companyId);

        // 데이터 부족하면 판단 불가
        if (stocks.size() < 5) {
            log.warn("거래량 데이터 부족: companyId={}, size={}", companyId, stocks.size());
            return 0.0;
        }

        // 최신 데이터 = 오늘
        StockEntity today = stocks.get(0);
        long todayVolume = today.getAcmlVol();

        // 최근 4일 평균
        double avgVolume4d = stocks.subList(1, 5).stream()
                .mapToLong(StockEntity::getAcmlVol)
                .average()
                .orElse(0.0);

        if (avgVolume4d == 0) {
            log.warn("4일 평균 거래량 0 → 계산 스킵: companyId={}", companyId);
            return 0.0;
        }

        double volumeRate =
                ((todayVolume - avgVolume4d) / avgVolume4d) * 100.0;

        log.debug(
            "VolumeRate 계산: companyId={}, today={}, avg4d={}, rate={}%",
            companyId, todayVolume, avgVolume4d, volumeRate
        );

        return volumeRate;
    }
}
