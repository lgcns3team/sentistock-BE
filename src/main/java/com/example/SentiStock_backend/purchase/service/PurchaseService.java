package com.example.SentiStock_backend.purchase.service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseRequestDto;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseResponseDto;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import com.example.SentiStock_backend.user.domain.dto.UserPurchaseResponseDto;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

        private final PurchaseRepository purchaseRepository;
        private final UserRepository userRepository;
        private final CompanyRepository companyRepository;
        private final StockRepository stockRepository;

        @Transactional
        public PurchaseResponseDto savePurchase(PurchaseRequestDto request) {

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

                return new PurchaseResponseDto(
                                purchase.getId(),
                                request.getCompanyId(),
                                request.getAvgPrice());
        }

        // public List<UserPurchaseResponseDTO> getMyPurchases(Long userId) {

        // List<PurchaseEntity> purchases = purchaseRepository.findByUser_Id(userId);

        // return purchases.stream()
        // .map(p -> UserPurchaseResponseDTO.builder()
        // .companyId(p.getCompany().getId()) // PK = company_id
        // .companyName(p.getCompany().getName()) // 회사명
        // .avgPrice(p.getAvgPrice())
        // .build())
        // .toList();
        // }

        public List<UserPurchaseResponseDto> getMyPurchases(Long userId) {

                List<PurchaseEntity> purchases = purchaseRepository.findByUser_Id(userId);

                return purchases.stream().map(purchase -> {

                        String companyId = purchase.getCompany().getId();
                        String companyName = purchase.getCompany().getName();
                        Float avgPrice = purchase.getAvgPrice();

                        StockEntity latestStock = stockRepository
                                        .findTopByCompanyIdOrderByDateDesc(companyId)
                                        .orElse(null);

                        Long currentPrice = (latestStock != null)
                                        ? latestStock.getStckPrpr()
                                        : null;

                        Double profitRate = null;

                        if (currentPrice != null && avgPrice != null && avgPrice > 0) {
                                double rate = ((currentPrice.doubleValue() - avgPrice.doubleValue())
                                                / avgPrice.doubleValue()) * 100;
                                profitRate = Double.valueOf(String.format("%.2f", rate));
                        }

                        return UserPurchaseResponseDto.builder()
                                        .companyId(companyId)
                                        .companyName(companyName)
                                        .avgPrice(avgPrice)
                                        .currentPrice(currentPrice)
                                        .profitRate(profitRate)
                                        .build();

                }).toList();
        }
}
