package com.example.SentiStock_backend.stock.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourStart = now.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime hourEnd = hourStart.plusHours(1);
        List<StockEntity> hourBars = stockRepository
                .findByCompany_IdAndDateBetweenOrderByDateAsc(
                        companyId,
                        hourStart,
                        hourEnd);

        long hourVolume = 0L;
        if (!hourBars.isEmpty()) {
            long max = hourBars.stream()
                    .mapToLong(StockEntity::getAcmlVol)
                    .max()
                    .orElse(0L);

            long min = hourBars.stream()
                    .mapToLong(StockEntity::getAcmlVol)
                    .min()
                    .orElse(0L);

            hourVolume = Math.max(0L, max - min);
        }
        return StockChangeInfo.builder()
                .currentPrice(current)
                .changeRate(changeRate)
                .volume(hourVolume)
                .build();
    }

    private List<StockHeatmapItemDto> loadSectorStocks(Long sectorId) {

        List<CompanyEntity> companies = companyRepository.findBySectorId(sectorId);

        List<StockHeatmapItemDto> result = new ArrayList<>();

        for (CompanyEntity company : companies) {

            StockChangeInfo info = getLatestPriceAndChange(company.getId());
            if (info == null) {
                continue;
            }

            result.add(
                    StockHeatmapItemDto.builder()
                            .companyId(company.getId())
                            .companyName(company.getName())
                            .currentPrice(info.getCurrentPrice())
                            .changeRate(info.getChangeRate())
                            .volume(info.getVolume())
                            .build());
        }

        return result;
    }

    public List<StockHeatmapItemDto> getSectorHeatmap(Long sectorId) {
        return loadSectorStocks(sectorId).stream()
                .sorted(Comparator.comparingLong(StockHeatmapItemDto::getVolume).reversed())
                .limit(16)
                .toList();
    }

    public List<StockHeatmapItemDto> getSectorRealtimeMonitor(Long sectorId) {
        // 상승률 순
        return loadSectorStocks(sectorId).stream()
                .sorted(Comparator.comparingDouble(StockHeatmapItemDto::getChangeRate).reversed())
                .limit(20)
                .toList();
    }

    public List<StockPriceDto> getHourlyCandles(String companyId) {

        LocalDateTime fromDate = LocalDate.now()
                .minusDays(10)
                .atTime(9, 0);

        List<StockEntity> entities = stockRepository
                .findByCompany_IdAndDateGreaterThanEqualOrderByDateAsc(companyId, fromDate);

        Map<LocalDateTime, List<StockEntity>> grouped = entities.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().truncatedTo(ChronoUnit.HOURS)));

        List<StockPriceDto> candles = grouped.entrySet().stream()
                .filter(entry -> {
                    int hour = entry.getKey().getHour();
                    return hour >= 9 && hour <= 16;
                })
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {

                    List<StockEntity> sorted = entry.getValue().stream()
                            .sorted(Comparator.comparing(StockEntity::getDate))
                            .toList();

                    List<StockEntity> valid = sorted.stream()
                            .filter(e -> e.getStckPrpr() != null && e.getStckPrpr() > 0 &&
                                    e.getStckOprc() != null && e.getStckOprc() > 0 &&
                                    e.getStckHgpr() != null && e.getStckHgpr() > 0 &&
                                    e.getStckLwpr() != null && e.getStckLwpr() > 0)
                            .toList();

                    if (valid.isEmpty()) {
                        return null;
                    }
                    long open = valid.get(0).getStckPrpr();
                    long close = valid.get(valid.size() - 1).getStckPrpr();
                    long high = valid.stream().mapToLong(StockEntity::getStckPrpr).max().orElse(0L);
                    long low = valid.stream().mapToLong(StockEntity::getStckPrpr).min().orElse(0L);

                    long sum = valid.stream().mapToLong(StockEntity::getStckPrpr).sum();
                    int avgPrice = (int) (sum / valid.size());

                    long maxVol = valid.stream().mapToLong(StockEntity::getAcmlVol).max().orElse(0L);
                    long minVol = valid.stream().mapToLong(StockEntity::getAcmlVol).min().orElse(0L);
                    long volume = Math.max(0L, maxVol - minVol);

                     return StockPriceDto.builder()
                        .date(entry.getKey())
                        .open(open)
                        .close(close)
                        .high(high)
                        .low(low)
                        .volume(volume)
                        .avgPrice(avgPrice)
                        .build();
                })
                .filter(dto -> dto != null)
                .toList();

        if (candles.size() > 28) {
            return candles.subList(candles.size() - 28, candles.size());
        }

        return candles;
    }

}
