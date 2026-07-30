# All about this service

A Spring Boot-based cryptocurrency trading application that provides APIs for user authentication, asset management, wallet operations, and order handling. Features JWT-based security, Postgres database integration, and Docker support.

## How to run the service

### Prerequisites
- Maven (via mvnw)
- Docker & Docker Compose
- Postgres database

### Build and Run

```bash
# Build the application
cd "D:\download files\projects\trading_app"
.\mvnw.cmd clean package -DskipTests

# Start services with Docker Compose
docker-compose up --build
```

Your API will be available at: `http://localhost:8080`

### Environment Configuration
- **Server Port**: 8080
- **Database**: Postgres at `jdbc:postgresql://postgres-db:5432/trading_app`
- **Default Credentials**: `root` / `user123`
- **Authentication**: JWT Token-based

```mermaid
flowchart LR

%% =====================================================
%% CLIENT
%% =====================================================

Client["🌐 Client<br/>(Browser / Mobile App / REST Client)"]

Client -->|HTTP Request| RateLimiter["RateLimitInterceptor"]
RateLimiter -->|Allowed| Security["Spring Security"]
RateLimiter -.->|"429 Too Many Requests"| Reject429["Request Rejected"]

Security --> Jwt["JwtTokenValidator Filter"]
Jwt -->|"JWT Valid"| Router
Jwt -.->|"401 Unauthorized"| Reject401["Authentication Failed"]

%% =====================================================
%% CONTROLLERS
%% =====================================================

subgraph Router["REST API Controllers"]

direction TB

Auth["🔐 AuthController

POST /auth/signup
POST /auth/signin
POST /auth/verify-otp
POST /auth/logout"]

User["👤 UserController

GET /users/profile
PUT /users/profile"]

Wallet["💰 WalletController

GET /wallet
GET /wallet/history"]

Crypto["📈 CryptoCurrencyController

GET /crypto
GET /crypto/{symbol}
GET /crypto/search"]

Order["📝 OrderController

POST /orders
GET /orders
GET /orders/{id}
DELETE /orders/{id}"]

Payment["💳 PaymentController

POST /payments/stripe
POST /payments/razorpay"]

end

Router --> Auth
Router --> User
Router --> Wallet
Router --> Crypto
Router --> Order
Router --> Payment

%% =====================================================
%% SERVICES
%% =====================================================

subgraph Services["Business Service Layer"]

direction TB

AuthService["AuthService"]

UserService["UserService"]

WalletService["WalletService"]

CryptoService["CryptoCurrencyService"]

OrderService["OrderService"]

PaymentService["PaymentService"]

OtpService["TwoFactorOtpService"]

JwtProvider["JwtProvider"]

EmailService["EmailService"]

KafkaProducer["Kafka Producer"]

end

Auth --> AuthService
User --> UserService
Wallet --> WalletService
Crypto --> CryptoService
Order --> OrderService
Payment --> PaymentService

%% =====================================================
%% AUTH FLOW
%% =====================================================

AuthService -->|"Find User"| UserRepo
AuthService -->|"Generate OTP"| OtpService
AuthService -->|"Generate JWT"| JwtProvider

OtpService -->|"Store OTP"| Redis
OtpService -->|"Send Email"| EmailService
EmailService --> SMTP["SMTP Provider"]

JwtProvider -->|"JWT Response"| Client

%% =====================================================
%% USER FLOW
%% =====================================================

UserService --> UserRepo
UserService --> Redis

%% =====================================================
%% WALLET FLOW
%% =====================================================

WalletService --> WalletRepo
WalletService --> Redis

%% =====================================================
%% CRYPTO FLOW
%% =====================================================

CryptoService -->|"Read Prices"| Redis
CryptoService -. Cache Miss .-> Market["External Crypto API"]
Market --> Redis

%% =====================================================
%% ORDER FLOW
%% =====================================================

OrderService --> OrderRepo
OrderService --> WalletRepo
OrderService -->|"Publish Order Event"| KafkaProducer

KafkaProducer --> Kafka["Kafka Cluster"]

Kafka --> Worker["Background Order Processor"]

Worker --> OrderRepo
Worker --> WalletRepo
Worker --> Notification["Notification Service"]

Notification --> EmailService

%% =====================================================
%% PAYMENT FLOW
%% =====================================================

PaymentService --> Stripe["Stripe API"]
PaymentService --> Razorpay["Razorpay API"]

Stripe -->|"Payment Status"| PaymentService
Razorpay -->|"Payment Status"| PaymentService

PaymentService --> PaymentRepo

PaymentService -->|"Publish Payment Event"| KafkaProducer

%% =====================================================
%% REPOSITORIES
%% =====================================================

subgraph Repository["Spring Data JPA"]

direction TB

UserRepo["UserRepository"]

WalletRepo["WalletRepository"]

OrderRepo["OrderRepository"]

PaymentRepo["PaymentRepository"]

CryptoRepo["CryptoRepository"]

end

UserRepo --> PostgreSQL
WalletRepo --> PostgreSQL
OrderRepo --> PostgreSQL
PaymentRepo --> PostgreSQL
CryptoRepo --> PostgreSQL

%% =====================================================
%% DATABASES
%% =====================================================

PostgreSQL[("🐘 PostgreSQL")]

Redis[("⚡ Redis

• Cache
• OTP
• Sessions
• Rate Limits")]

%% =====================================================
%% OBSERVABILITY
%% =====================================================

subgraph Infra["Infrastructure"]

Actuator["Spring Boot Actuator"]

Prometheus["Prometheus"]

Grafana["Grafana"]

Docker["Docker"]

end

Actuator --> Prometheus
Prometheus --> Grafana

Docker -. Deploys .-> Router
Docker -. Deploys .-> Services

%% =====================================================
%% STYLING
%% =====================================================

classDef controller fill:#D6EAF8,stroke:#1F618D,stroke-width:2px;
classDef service fill:#D5F5E3,stroke:#1E8449,stroke-width:2px;
classDef repo fill:#FCF3CF,stroke:#B7950B,stroke-width:2px;
classDef db fill:#FADBD8,stroke:#922B21,stroke-width:2px;
classDef ext fill:#EBDEF0,stroke:#6C3483,stroke-width:2px;

class Auth,User,Wallet,Crypto,Order,Payment controller;
class AuthService,UserService,WalletService,CryptoService,OrderService,PaymentService,OtpService,JwtProvider,EmailService,KafkaProducer service;
class UserRepo,WalletRepo,OrderRepo,PaymentRepo,CryptoRepo repo;
class PostgreSQL,Redis db;
class Kafka,Stripe,Razorpay,SMTP,Market,Worker,Notification ext;
```

