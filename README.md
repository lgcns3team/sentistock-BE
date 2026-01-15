# sentistock-BE

> **뉴스 감정 분석 기반 주식 투자 판단 지원 백엔드 서비스**  
> 뉴스 데이터를 수집·분석하여 감정 지표를 생성하고,  
> 주가 데이터와 결합해 투자 판단에 참고할 수 있는 정보를 제공합니다.

## 🔗 Tech Stack

### Language & Framework
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security (JWT / OAuth2)
- Spring Validation
> 최신 LTS기반으로 안정성과 성능을 확보하고 인증,인가 및 도메인 중심 설계를 위해 Spring 적극 활용

### Database
- MariaDB (AWS RDS)
- JPA / Hibernate
- Transaction Management
> 사용자, 주식, 뉴스, 감정 데이터 등 관계형 데이터 중심의 도메인 구조 관리

### Messaging & Event Streaming
- Apache Kafka (AWS MSK)
- Kafka Producer/Consumer
> 감정 분석 결과, 주식 이벤트, 알림 트리거를 API 요청 흐름과 분리하여 비동기 처리

### Notification
- Firebase Cloud Messaging(FCM)
> 매수, 매도 시그널 발생 시 사용자에게 실시간 푸시 알림 전송

### Infrastructure & Cloud
- AWS EKS
- AWS ALB (Ingress)
- AWS ECR
- AWS RDS
- CloudFront + S3(Frontend 연동)
> 컨테이너 기반 배포 환경 구성, 서비스 확장성과 운영 안정성 확보

### Infrastructure as Code (IaC)
- Terraform
> VPC, EKS, RDS, MSK 등 AWS 인프라를 코드로 관리하여 재현성과 일관성 유지

### Container & Orchestration
- Docker
- Kuberbetes Deployment/Service/Ingress
- Kubernetes Cronjob
> 실시간 API 서버와 뉴스 수집, 감정 분석 배치 작업을 분리 운영

### Monitoring & Docs
- Swagger (OpenAPI)
> 프론트엔드와의 협업 API 명세 공유를 위해 활용

### 
---

## 🔐 Authentication & Authorization
### JWT 기반 인증 구조
> - Access Token / Refresh Token 분리 구조
> - 세션을 저장하지 않는 Stateless 방식
> - 토큰 기반 인증으로 서버 확장(EKS 환경)에 유리

### Spring Security 기반 인증, 인가 처리
> - Spring Security Filter Chain 구성
> - Custom Authentication Filter를 통해 JWT 검증
> - 토큰 검증 실패 시 인증 예외 처리

#### 인가
> - 인증이 필요한 API와 공개 API를 명확히 구분
> - Gateway -> Backend 구조에서도 일관된 인증 흐름 유지

### OAuth2 소셜 로그인(Kakao)
> - OAuth2 Authorization Code Flow 적용
> - 기존 유저 / 신규 유저 분기 로직 구현

👉 소셜 로그인 이후에도
JWT 기반 인증 구조로 통합 관리하여
일반 로그인과 동일한 보안 흐름 유지

### Gateway 연계 인증 흐름
> - 모든 외부 요청은 Gateway를 단일 진입점으로 통과
> - Gateway에서 인증 헤더 전달
> - Backend는 비즈니스 로직에만 집중

👉 인증 책임을 분리하여
확장성과 유지보수성을 고려한 구조

## 📊 Core Features

## 🧪 API Documentation
- Swagger UI 제공

```
/swagger-ui/index.html
```

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

### ☸️ Kubernetes (AWS EKS)
> 본 프로젝트의 백엔드는 Amazon Web Services EKS 환경에서
컨테이너 기반으로 운영되며, 서비스 특성에 맞게 실시간 요청 처리와 배치 작업을 분리하여 구성

#### 🔹 EKS 기반 클러스터 구성
> - AWS EKS를 사용해 Kubernetes 클러스터 구성
> - Backend, Gateway, 배치 작업을 각각 독립적인 Pod로 운영
> - 컨테이너 단위 배포를 통해 환경 차이 없는 실행 환경 유지
> - Pod 장애 발생 시 자동 재시작으로 서비스 안정성 확보

#### 🔹 Deployment / Service / Ingress 구조

##### Deployment
> - Backend, Gateway를 각각 Deployment로 관리
> - 이미지 변경 시 Rolling Update 방식으로 배포

##### Service
> - Gateway에서 Backend로의 요청을 클러스터 내부 네트워크에서 처리
> - ClusterIP Service를 사용해 Pod 간 내부 통신 구성

##### Ingress(AWS ALB)
> - AWS ALB Ingress를 사용해 외부 트래픽을 클러스터로 유입
> - Gateway를 단일 진입점으로 설정
> - 경로 기반 라우팅 구성
(/api, /auth, /board)

#### 🔹 Batch 작업 분리 (CronJob)
> - 뉴스 수집, 감정 분석, 점수 계산 작업을 Kubernetes CronJob으로 실행
> - 작업 시에만 Pod를 생성하고 완료 후 자동 종료
> - 실시간 API 서버와 완전히 분리된 실행 환경

👉 무거운 연산 작업이 실시간 서비스에 영향을 주지 않도록 부하 분산

---

## 🧠 Key Design Decisions

- Gateway 분리로 인증/라우팅 책임 명확화
- Kafka 도입으로 서비스 간 결합도 감소
- EKS + Private Subnet 구성으로 보안 강화
- Terraform을 통한 인프라 재현성 확보

---

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
