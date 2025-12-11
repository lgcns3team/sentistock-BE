package com.example.SentiStock_backend.purchase.ctrl;

import com.example.SentiStock_backend.purchase.domain.dto.PurchaseRequestDto;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseResponseDto;
import com.example.SentiStock_backend.purchase.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseCtrl {

    private final PurchaseService purchaseService;

    @Operation(summary = "사용자 매수 종목 등록", description = "사용자가 매수한 종목 정보를 등록합니다.")
    @PostMapping("/save")
    public ResponseEntity<PurchaseResponseDto> savePurchase(
            @Parameter(description = "매수 종목 정보", example = "{ \"userId\": 1, \"companyId\": \"005930\", \"companyName\": \"삼성전자\", \"purchasePrice\": 60000, \"quantity\": 10 }") @RequestBody PurchaseRequestDto request) {
        return ResponseEntity.ok(purchaseService.savePurchase(request));
    }
}
