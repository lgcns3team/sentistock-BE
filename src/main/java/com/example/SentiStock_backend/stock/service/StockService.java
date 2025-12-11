package com.example.SentiStock_backend.stock.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.stock.domain.dto.StockChangeInfo;
import com.example.SentiStock_backend.stock.domain.dto.StockHeatmapItemDto;
import com.example.SentiStock_backend.stock.domain.dto.StockPriceDto;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public StockChangeInfo getLatestPriceAndChange(String companyId) {

        StockEntity latestStock = stockRepository
                .findTopByCompanyIdOrderByDateDesc(companyId)
                .orElse(null);

        if (latestStock == null) {
            return null;
        }

        Long current = latestStock.getStckPrpr(); // 현재가
        Long prevClose = latestStock.getStckPrdyClpr(); // 전일종가

        Double changeRate = null;
        if (current != null && prevClose != null && prevClose != 0) {
            long diff = current - prevClose;
            changeRate = diff * 100.0 / prevClose;
        }

        return StockChangeInfo.builder()
                .currentPrice(current)
                .changeRate(changeRate)
                .build();
    }

    public List<StockHeatmapItemDto> getSectorHeatmap(Long sectorId) {

        // 1) 섹터에 속한 종목들 찾기
        List<CompanyEntity> companies = companyRepository.findBySectorId(sectorId);

        List<StockHeatmapItemDto> result = new ArrayList<>();

        // 2) 각 종목별로 최신 시세 한 건 조회
        for (CompanyEntity company : companies) {

            StockChangeInfo info = getLatestPriceAndChange(company.getId());
            if (info == null) {
                continue; 
            }

            // 3) DTO로 매핑해서 리스트에 추가
            result.add(
                    StockHeatmapItemDto.builder()
                            .companyId(company.getId())
                            .companyName(company.getName())
                            .currentPrice(info.getCurrentPrice())
                            .changeRate(info.getChangeRate())
                            .build());
        }

        return result;
    }

    public List<StockPriceDto> getHourlyCandles(String companyId) {

        LocalDateTime startOfDay = LocalDate.now().atTime(9, 0);
        // 오늘 날짜의 09:00:00 을 생성

        List<StockEntity> entities = stockRepository
                .findByCompany_IdAndDateGreaterThanEqualOrderByDateAsc(companyId, startOfDay);
        // DB에서 companyId AND date >= 오늘 09:00:00 조건으로 데이터 조회

        Map<LocalDateTime, List<StockEntity>> grouped = entities.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate()
                                .truncatedTo(ChronoUnit.HOURS)));
        // 날짜를 시 단위(ChronoUnit.HOURS)로 잘라서 그룹핑

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<StockEntity> list = entry.getValue();

                    return StockPriceDto.builder()
                            .date(entry.getKey())
                            .open(list.get(0).getStckOprc())
                            .close(list.get(list.size() - 1).getStckPrpr())
                            .high(list.stream().mapToLong(StockEntity::getStckHgpr).max().orElse(0))
                            .low(list.stream().mapToLong(StockEntity::getStckLwpr).min().orElse(0))
                            .volume(list.stream().mapToLong(StockEntity::getAcmlVol).sum())
                            .build();
                })
                .toList();
    }

}
