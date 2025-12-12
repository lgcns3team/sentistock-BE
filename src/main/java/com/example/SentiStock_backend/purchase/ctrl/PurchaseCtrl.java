package com.example.SentiStock_backend.purchase.ctrl;

import com.example.SentiStock_backend.auth.jwt.CustomUserDetails;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseRequestDto;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseResponseDto;
import com.example.SentiStock_backend.purchase.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
@Tag(name = "Purchase API", description = "사용자 매수 종목 등록 관련 API")
public class PurchaseCtrl {

    private final PurchaseService purchaseService;

    @Operation(summary = "사용자 매수 종목 등록", description = "사용자가 매수한 종목 정보를 등록합니다.")
    @PostMapping("/save")
    public ResponseEntity<PurchaseResponseDto> savePurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails, 
            @RequestBody PurchaseRequestDto request) {

        return ResponseEntity.ok(purchaseService.savePurchase(userDetails.getId(), request));
    }
}
