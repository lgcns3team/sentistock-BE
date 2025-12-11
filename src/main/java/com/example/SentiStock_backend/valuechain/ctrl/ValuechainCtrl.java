package com.example.SentiStock_backend.valuechain.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.valuechain.domain.dto.ValuechainResponseDto;
import com.example.SentiStock_backend.valuechain.service.ValuechainService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/valuechains")
@Tag(name = "Valuechain API", description = "기업 간 Value Chain(밸류체인) 관계를 조회하는 API")
public class ValuechainCtrl {

    @Autowired
    private ValuechainService valuechainService;

    @GetMapping("/{companyId}")
    @Operation(summary = "기업 밸류체인 조회", description = "특정 기업이 Value Chain 상에서 어떤 기업들과 연결되어 있는지 조회합니다")
    public ResponseEntity<List<ValuechainResponseDto>> getValuechainsByCompany(
        @Parameter(description = "기업 ID (예: 005930, 000660)", example = "005930")    
        @PathVariable String companyId) {
        List<ValuechainResponseDto> result = valuechainService.getValuechainsByFromCompanyId(companyId);

        return ResponseEntity.ok(result);
    }
}
