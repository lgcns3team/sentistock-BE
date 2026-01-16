# sentistock-BE

> **뉴스 감정 분석 기반 주식 투자 판단 지원 백엔드 서비스**  
> 뉴스 데이터를 수집·분석하여 감정 지표를 생성하고,  
> 주가 데이터와 결합해 투자 판단에 참고할 수 있는 정보를 제공합니다.

## 📦 Repositories

[![Frontend](https://img.shields.io/badge/Frontend-sentistock--FE-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://github.com/lgcns3team/sentistock-FE)
[![Infra](https://img.shields.io/badge/Infrastructure-sentistock--infra-844FBA?style=for-the-badge&logo=terraform&logoColor=white)](https://github.com/lgcns3team/sentistock-infra)
[![Gateway](https://img.shields.io/badge/API%20Gateway-sentistock--scg-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://github.com/lgcns3team/sentistock-scg)

[![News Crawler](https://img.shields.io/badge/Crawler-news-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://github.com/lgcns3team/crawling-news)
[![Stocks Crawler](https://img.shields.io/badge/Crawler-stocks-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://github.com/lgcns3team/crawling-stocks)
[![Backtest](https://img.shields.io/badge/Analysis-backtest-000000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/lgcns3team/sentistock-backtest)


<details>
<summary><h2>📂 프로젝트구조</h2></summary>
<div markdown="1">    
 
```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂SentiStock_backend
 ┃ ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SignUpRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenReissueRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshToken.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetails.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtTokenProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂oauth
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoTokenResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoUserInfoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoOAuthService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RefreshTokenRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenHashUtil.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂company
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CompanyCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CompanyDetailDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CompanyEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CompanyRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CompanyService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KafkaConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SwaggerConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂event
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StockEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StockEventProducer.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StockEventType.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂favorite
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteCompanyCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteCompanyResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteSectorResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteStatusResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteCompanyEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteCompanyId.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteSectorEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteSectorId.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteCompanyRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteSectorRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FavoriteCompanyService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FavoriteSectorService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂health
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜HealthCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂news
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NewsCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NewsSentimentDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NewsEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NewsRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NewsService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂notification
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FirebaseConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂consumer
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationConsumer.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationSettingCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂decision
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationDecisionService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationSettingRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationSettingResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationSettingEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂type
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationSettingRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FirebaseService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationSettingService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂purchase
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PurchaseCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PurchaseDeleteRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PurchaseRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PurchaseResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PurchaseEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PurchaseRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PurchaseService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂scheduler
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StockEventScheduler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂sector
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SectorEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SectorRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂sentiment
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SentimentCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SentimentRatioResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SentimentResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StocksScoreResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SentimentEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StocksScoreEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SentimentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StocksScoreRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SentimentService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂stock
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StockCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StockChangeInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StockHeatmapItemDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StockPriceDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StockEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜StockRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜StockService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜VolumeService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂trade
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entitiy
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TradeSignalEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TradeSignalRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TradeDecisionService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂type
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TradeDecisionType.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SubscriptionCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FcmTokenRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OnboardingRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PasswordChangeRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SubscriptionInfoResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMeResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserProfileUpdateRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserPurchaseResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SubscriptionService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserInvestorService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂valuechain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ctrl
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValuechainCtrl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValuechainResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValuechainEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValuechainRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ValuechainService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SentiStockBackendApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📜application-eks.yaml
 ┃ ┃ ┣ 📜application-local.yaml
 ┃ ┃ ┣ 📜application-prod.yml
 ┃ ┃ ┗ 📜application.yaml
 ┗ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂SentiStock_backend
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SentiStockBackendApplicationTests.java
```

</div>
</details>


## 🔗 Tech Stack

### Language & Framework
![Java](https://img.shields.io/badge/Java%2017-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

### Database
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Amazon RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)

### Messaging & Event Streaming
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Amazon MSK](https://img.shields.io/badge/AWS%20MSK-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)

### Notification
![Firebase](https://img.shields.io/badge/Firebase%20Cloud%20Messaging-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

### Infrastructure & Cloud
![Amazon EKS](https://img.shields.io/badge/AWS%20EKS-FF9900?style=for-the-badge&logo=amazoneks&logoColor=white)
![AWS ALB](https://img.shields.io/badge/AWS%20ALB-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Amazon ECR](https://img.shields.io/badge/AWS%20ECR-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![CloudFront](https://img.shields.io/badge/AWS%20CloudFront-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Amazon S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)

### IaC
![Terraform](https://img.shields.io/badge/Terraform-844FBA?style=for-the-badge&logo=terraform&logoColor=white)

### Container & Orchestration
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)

### Monitoring & Docs
![Swagger](https://img.shields.io/badge/Swagger%20-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

### Security & Core Concepts
![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge)
![OAuth2](https://img.shields.io/badge/OAuth2-Authorization-blue?style=for-the-badge)

### Notification
![Firebase Cloud Messaging](https://img.shields.io/badge/Firebase-Cloud%20Messaging-FFCA28?style=for-the-badge)

---
## 📌 Key APIs

| Feature | Method | Endpoint | Description |
|---|---|---|---|
| Health | GET | `/health` | 서버 상태 확인 |
| Auth | POST | `/api/auth/signup` | 회원가입 |
| User | GET | `/api/users/me` | 내 정보 조회 |
| User | POST | `/api/users/me/fcm-token` | FCM 토큰 업데이트 |
| Company | GET | `/api/companies/{companyId}/snapshot` | 종목 스냅샷(현재가/등락률) |
| Favorites | POST | `/api/companies/{companyId}/favorite/star` | 종목 즐겨찾기 토글 |
| Stock | GET | `/api/stock/{sectorId}/heatmap` | 섹터 히트맵 조회 |
| Stock | GET | `/api/stock/candle/hourly/{companyId}` | 시간봉 캔들 조회 |
| News | GET | `/api/news/recent-score/{companyId}` | 최신 뉴스 3건 + 감정 점수 |
| Sentiment | GET | `/api/sentiment/history/{companyId}` | 감정 히스토리 조회 |
| Purchase | POST | `/api/purchase/save` | 매수 종목 등록 |
| Subscription | POST | `/api/subscriptions/start` | 구독 시작 |
| Notification | GET | `/api/notifications` | 내 알림 목록 조회 |
| Notification | PATCH | `/api/notifications/{notificationId}/check` | 알림 읽음 처리 |
| Valuechain | GET | `/api/valuechains/{companyId}` | 기업 밸류체인 조회 |

---
## Deployment

### Build
```
./gradlew build
```

### Docker
```
docker build -t sentistock-backend .
docker push <ECR_REPOSITORY>
```
---
## Infra
<img width="1875" height="1077" alt="image" src="https://github.com/user-attachments/assets/8b3a91eb-db74-4b16-a716-aa90e94630bf" />

>본 서비스는 AWS EKS 기반의 컨테이너 환경에서 API 서버와 배치 작업을 분리하여 운영합니다.
>외부 트래픽은 ALB를 통해서만 유입되며, 모든 서비스 로직은 Private Subnet의 EKS에서 처리됩니다.
>프론트엔드는 CloudFront + S3로 정적 배포하여 빠른 응답을 제공하고,
>백엔드는 Kafka(MSK)를 활용해 감정 분석 및 주식 이벤트를 비동기로 처리함으로써
>API 성능과 확장성을 동시에 확보했습니다.
>VPC, EKS, RDS, MSK 등 모든 인프라는 Terraform으로 관리하여
>재현성과 일관성을 유지합니다.

[![GitHub Repo](https://img.shields.io/badge/GitHub-sentistock--infra-181717?style=for-the-badge&logo=github)](https://github.com/lgcns3team/sentistock-infra)

---
## ☸️ Kubernetes (AWS EKS)

본 프로젝트의 백엔드는 **AWS EKS 기반 Kubernetes 환경**에서  
컨테이너 단위로 운영되며, 실시간 API와 배치 작업을 분리하여 구성했습니다.

### EKS Cluster 구성
- AWS EKS를 사용한 관리형 Kubernetes 클러스터
- Backend, Gateway, Batch 작업을 각각 독립적인 Pod로 운영
- 컨테이너 기반 배포로 환경 차이 없는 실행 환경 유지
- Pod 장애 발생 시 자동 재시작으로 서비스 안정성 확보

### Deployment / Service / Ingress
**Deployment**
- Backend, Gateway를 각각 Deployment로 관리
- 이미지 변경 시 Rolling Update 방식으로 배포

**Service**
- Gateway ↔ Backend 간 통신은 ClusterIP Service 사용
- 클러스터 내부 네트워크를 통한 안전한 서비스 연결

**Ingress (AWS ALB)**
- AWS ALB Ingress를 통해 외부 트래픽 유입
- Gateway를 단일 진입점으로 구성
- 경로 기반 라우팅 적용 (`/api`, `/auth`, `/board`)

### Batch 작업 (CronJob)
- 뉴스 수집, 감정 분석, 점수 계산 작업을 Kubernetes CronJob으로 실행
- 작업 시에만 Pod를 생성하고 완료 후 자동 종료
- 실시간 API 서버와 분리된 실행 환경


> 무거운 연산 작업이 실시간 서비스에 영향을 주지 않도록  
> **부하 분산과 안정성을 고려한 구조**입니다.

---
## 🔄 CI / CD

본 프로젝트는 **GitHub Actions 기반 CI/CD 파이프라인**을 구성하여  
코드 변경부터 컨테이너 이미지 배포까지 자동화했습니다.

### CI (Continuous Integration)
- GitHub Actions를 통해 코드 변경 시 자동 빌드
- Spring Boot 애플리케이션을 Docker 이미지로 패키징
- 빌드된 이미지를 **AWS ECR**에 자동 푸시

### CD (Continuous Deployment)
- ECR에 푸시된 이미지를 기준으로 Kubernetes 배포 환경에서 사용
- 이미지 태그를 통해 버전 관리
- 무중단 배포 및 확장 가능한 컨테이너 운영 환경 구성

> CI/CD 자동화를 통해 배포 과정의 안정성과 일관성을 확보했습니다.
---

## 🔐 Authentication & Authorization

본 서비스는 **JWT 기반 Stateless 인증 구조**를 사용하여  
컨테이너 환경(EKS)에서도 확장성과 일관된 보안을 유지하도록 설계했습니다.

### JWT Authentication
- Access Token / Refresh Token 분리
- 서버 세션을 사용하지 않는 Stateless 방식
- 수평 확장에 유리한 인증 구조

### Spring Security
- Filter Chain 기반 인증·인가 처리
- JWT 검증을 위한 커스텀 필터 적용
- 인증 실패 시 공통 예외 처리

### OAuth2 Social Login (Kakao)
- OAuth2 Authorization Code Flow 적용
- 소셜 로그인 이후 JWT 발급
- 일반 로그인과 동일한 인증 흐름으로 통합 관리

### Gateway 기반 인증 흐름
- 모든 외부 요청은 Gateway를 단일 진입점으로 처리
- 인증 정보는 헤더로 전달
- Backend는 비즈니스 로직에 집중

> 인증 책임을 분리하여 **확장성과 유지보수성을 고려한 구조**입니다.
---

<div align=center>
	<h1>👑 BE Developers 👑</h1>
	
| <img src="https://github.com/rudals2334.png" width="80"> | <img src="https://github.com/M4rs0312.png" width="80"> | <img src="https://github.com/jun9ho.png" width="80"> |
| :--------------------------------------------------: | :------------------------------------------------------: | :----------------------------------------------------------: | 
|         [이경민](https://github.com/rudals2334)          |         [정회성](https://github.com/M4rs0312)          |         [황정호](https://github.com/jun9ho)     |                         
|                       BackEnd                       |                         BackEnd                         |                           BackEnd                           |                                     

</div>
