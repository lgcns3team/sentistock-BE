# sentistock-BE
Sentistock 백엔드

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
