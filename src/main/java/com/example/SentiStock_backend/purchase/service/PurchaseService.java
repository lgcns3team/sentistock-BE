package com.example.SentiStock_backend.purchase.service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseRequestDTO;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseResponseDTO;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public PurchaseResponseDTO savePurchase(PurchaseRequestDTO request) {

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        CompanyEntity company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company Not Found"));

        PurchaseEntity purchase = PurchaseEntity.builder()
                .user(user)
                .company(company)
                .avgPrice(request.getAvgPrice())
                .build();

        purchaseRepository.save(purchase);

        return new PurchaseResponseDTO(
                purchase.getId(),
                request.getCompanyId(),
                request.getAvgPrice()
        );
    }
}
