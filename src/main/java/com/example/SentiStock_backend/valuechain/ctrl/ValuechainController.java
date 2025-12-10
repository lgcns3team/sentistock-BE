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

@RestController
@RequestMapping("/api/v1/valuechains")
public class ValuechainController {

    @Autowired
    private ValuechainService valuechainService;

    @GetMapping("/{companyId}")
    public ResponseEntity<List<ValuechainResponseDto>> getValuechainsByCompany(
            @PathVariable String companyId) {
        List<ValuechainResponseDto> result = valuechainService.getValuechainsByFromCompanyId(companyId);

        return ResponseEntity.ok(result);
    }
}
