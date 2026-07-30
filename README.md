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

%% =========================
%% CLIENT
%% =========================

Client["🌐 Client<br/>Browser / Mobile / API"] -->|HTTPS Request| RateLimit["RateLimitInterceptor<br/>(Redis)"]
RateLimit -->|Allowed| Security["Spring Security Filter Chain"]
RateLimit -.->|Blocked| TooMany["429 Too Many Requests"]

Security --> JwtFilter["JwtTokenValidator"]
JwtFilter -->|Valid JWT| Controllers
JwtFilter -.->|Invalid JWT| Unauthorized["401 Unauthorized"]

%% =========================
%% APPLICATION
%% =========================

subgraph App["Trading Application (Spring Boot)"]

direction TB

Controllers["REST Controllers"]

Services["Business Services
• AuthService
• UserService
• WalletService
• OrderService
• CryptoCurrencyService
• PaymentService
• TwoFactorOtpService"]

Repositories["Spring Data JPA Repositories"]

JwtProvider["JwtProvider"]

RedisCache["Redis
• Cache
• Sessions
• OTP
• Rate Limits"]

Email["Email Service"]

KafkaProducer["Kafka Producer"]

Controllers --> Services
Services --> Repositories

end

%% =========================
%% DATABASES
%% =========================

Repositories -->|Read / Write| PostgreSQL[(Postgres)]

Services -->|Cache Reads| RedisCache
Services -->|Cache Updates| RedisCache

RedisCache --> Redis[(Redis)]

%% =========================
%% AUTHENTICATION FLOW
%% =========================

Services -->|Authenticate User| JwtProvider
JwtProvider -->|Generate JWT| Controllers
Controllers -->|JWT Response| Client

Services -->|Generate OTP| RedisCache
Services -->|Send OTP| Email
Email --> SMTP["SMTP Provider"]

%% =========================
%% PAYMENTS
%% =========================

Services -->|Create Payment| Stripe["Stripe API"]
Services -->|Create Payment| Razorpay["Razorpay API"]

Stripe -->|Payment Result| Services
Razorpay -->|Payment Result| Services

Services -->|Persist Payment| PostgreSQL

%% =========================
%% ORDER PROCESSING
%% =========================

Services -->|Publish Order Event| KafkaProducer

KafkaProducer --> Kafka[(Kafka Cluster)]

Kafka --> Workers["Background Workers"]

Workers -->|Execute Order| PostgreSQL
Workers -->|Update Wallet| PostgreSQL
Workers -->|Publish Status| Kafka

%% =========================
%% OBSERVABILITY
%% =========================

App --> Actuator["Spring Boot Actuator"]

Actuator --> Prometheus["Prometheus"]

Prometheus --> Grafana["Grafana"]

%% =========================
%% DEPLOYMENT
%% =========================

Docker["Docker"] -.-> App

%% =========================
%% STYLING
%% =========================

classDef infra fill:#FFF8DC,stroke:#444;
classDef ext fill:#FDE2E4,stroke:#444;
classDef app fill:#D8F3DC,stroke:#444;

class Controllers,Services,Repositories,JwtProvider,RedisCache,Email,KafkaProducer app;
class PostgreSQL,Redis,Kafka,Stripe,Razorpay,SMTP,Prometheus,Grafana ext;
class Client,Security,JwtFilter,RateLimit infra;
```
