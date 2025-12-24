package com.example.SentiStock_backend.company.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SentiStock_backend.company.domain.dto.CompanyDetailDto;
import com.example.SentiStock_backend.company.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company", description = "종목 정보 조회 API")
public class CompanyCtrl {

    @Autowired
    private CompanyService companyService;

    @Operation(
        summary = "종목 스냅샷 조회",
        description = """
            종목코드를 입력받아
            - 종목명
            - 현재가
            - 등락률(%)
            정보를 반환합니다.
            
            예시:
            - 005930 → 삼성전자
            """
    )
    @GetMapping("/{companyId}/snapshot")
    public ResponseEntity<CompanyDetailDto> getSnapshot(
        @Parameter(
            description = "종목 코드 (6자리)",
            example = "005930",
            required = true
        )
        @PathVariable String companyId
    ) {
        return ResponseEntity.ok(companyService.getSnapshot(companyId));
    }
}