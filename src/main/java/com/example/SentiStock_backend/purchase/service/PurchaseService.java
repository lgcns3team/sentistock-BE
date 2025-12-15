package com.example.SentiStock_backend.purchase.service;

import com.example.SentiStock_backend.company.domain.entity.CompanyEntity;
import com.example.SentiStock_backend.company.repository.CompanyRepository;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseRequestDto;
import com.example.SentiStock_backend.purchase.domain.dto.PurchaseResponseDto;
import com.example.SentiStock_backend.purchase.domain.entity.PurchaseEntity;
import com.example.SentiStock_backend.purchase.repository.PurchaseRepository;
import com.example.SentiStock_backend.sentiment.domain.entity.SentimentEntity;
import com.example.SentiStock_backend.sentiment.domain.entity.StocksScoreEntity;
import com.example.SentiStock_backend.sentiment.repository.SentimentRepository;
import com.example.SentiStock_backend.sentiment.repository.StocksScoreRepository;
import com.example.SentiStock_backend.stock.domain.entity.StockEntity;
import com.example.SentiStock_backend.stock.repository.StockRepository;
import com.example.SentiStock_backend.user.domain.dto.UserPurchaseResponseDto;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import com.example.SentiStock_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

        private final PurchaseRepository purchaseRepository;
        private final CompanyRepository companyRepository;
        private final StockRepository stockRepository;
        private final UserRepository userRepository;
        private final StocksScoreRepository stocksScoreRepository;

        @Transactional
        public PurchaseResponseDto savePurchase(Long userId, PurchaseRequestDto request) {

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User Not Found"));

                CompanyEntity company = companyRepository.findById(request.getCompanyId())
                                .orElseThrow(() -> new RuntimeException("Company Not Found"));

                Double latestSenti = stocksScoreRepository
                                .findTopByCompany_IdOrderByDateDesc(company.getId())
                                .map(StocksScoreEntity::getScore)
                                .orElse(null);

                PurchaseEntity purchase = PurchaseEntity.builder()
                                .user(user)
                                .company(company)
                                .avgPrice(request.getAvgPrice())
                                .purSenti(latestSenti)
                                .build();

                purchaseRepository.save(purchase);

                return new PurchaseResponseDto(
                                request.getCompanyId(),
                                request.getAvgPrice());
        }

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
