package com.example.SentiStock_backend.stock.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.stock.domain.dto.StockHeatmapItemDto;
import com.example.SentiStock_backend.stock.service.StockService;

@RestController
@RequestMapping("/api/v1/stock")
public class StockCtrl {
    @Autowired
    private StockService stockService;

    @GetMapping("/{sectorId}/heatmap")
    public ResponseEntity<List<StockHeatmapItemDto>> getSectorHeatmap(
            @PathVariable Long sectorId) {
        return ResponseEntity.ok(
                stockService.getSectorHeatmap(sectorId));

    }
}
