package com.example.SentiStock_backend.purchase.ctrl;

import com.example.SentiStock_backend.purchase.domain.dto.PurchaseRequestDTO;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseResponseDTO;
import com.example.SentiStock_backend.purchase.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseCtrl {

    private final PurchaseService purchaseService;

    @Operation(summary = "사용자 매수 종목 등록")
    @PostMapping("/save")
    public ResponseEntity<PurchaseResponseDTO> savePurchase(@RequestBody PurchaseRequestDTO request) {
        return ResponseEntity.ok(purchaseService.savePurchase(request));
    }
}
