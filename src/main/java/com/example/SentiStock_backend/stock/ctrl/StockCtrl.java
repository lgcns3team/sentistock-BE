package com.example.SentiStock_backend.stock.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.stock.domain.dto.StockHeatmapItemDto;
import com.example.SentiStock_backend.stock.domain.dto.StockPriceDto;
import com.example.SentiStock_backend.stock.service.StockService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/stock")
@Tag(
    name = "Stock API",
    description = "종목/섹터 관련 조회 API"
)
public class StockCtrl {
    @Autowired
    private StockService stockService;

    @GetMapping("/{sectorId}/heatmap")
    @Operation(
        summary = "섹터 히트맵 조회",
        description = "섹터 ID에 해당하는 종목들의 히트맵 데이터를 조회합니다."
    )
    public ResponseEntity<List<StockHeatmapItemDto>> getSectorHeatmap(
            @Parameter(description = "섹터 ID", example = "1")
            @PathVariable Long sectorId) {
        return ResponseEntity.ok(
                stockService.getSectorHeatmap(sectorId));

    }
    
    @GetMapping("/candle/hourly/{companyId}")
    @Operation(
        summary = "시간봉 캔들 조회",
        description = "지정된 회사 종목에 대한 시간봉 캔들 데이터를 조회합니다."
    )
    public ResponseEntity<List<StockPriceDto>> getCandles(
            @Parameter(description = "회사 종목 코드", example = "005930")
            @PathVariable String companyId) {
        return ResponseEntity.ok(stockService.getHourlyCandles(companyId));
    }
}
