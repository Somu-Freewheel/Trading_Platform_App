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
  %% Clients
  Client[Client\n(Browser / Mobile / API Consumer)] -->|HTTP JSON| RL[RateLimit\nInterceptor (Redis)]

  %% Application boundary
  subgraph App[Trading App - Spring Boot]
	direction TB
	RL --> Controllers[Controllers\n(AuthController, CryptoCurrencyController, OrderController, WalletController, PaymentController, ...)]
	Controllers --> Services[Services\n(UserService, AuthService, PaymentService, TwoFactorOtpService, ...)]
	Services --> Repos[Repositories (JPA)\n(UserRepository, OrderRepository, ...)]
	Services --> RedisCache[Redis Cache / Session / Rate Limit]
	Services --> Jwt[JwtProvider / Token Generation]
	Services --> EmailSrv[Email Service (SMTP)]
	Services --> Kafka[Kafka Producer]
  end

  %% Datastores and external systems
  Repos -->|JPA / SQL| Postgres[(Postgres Database)]
  RedisCache -->|fast reads/writes| Redis[(Redis)]
  EmailSrv -->|SMTP| Mail[SMTP / Mail Provider]
  Controllers -->|REST / SDK| Stripe[Stripe API]
  Controllers -->|REST / SDK| Razorpay[Razorpay API]
  Kafka -->|publish| KafkaCluster[(Kafka Cluster)]
  KafkaCluster -->|consume| Workers[Background Workers / Order Processors]
  Workers --> Repos

  %% Observability & deployment
  App -->|metrics/logs| Actuator[Spring Actuator / Prometheus]
  Docker[Docker / Docker Compose] -.-> App

  %% Notes
  classDef ext fill:#f9f,stroke:#333,stroke-width:1px;
  class Postgres,Redis,Stripe,Razorpay,Mail,KafkaCluster ext;
```

Notes:
- The `RateLimitInterceptor` consults Redis before requests reach the controllers to enforce signin limits.
- `JwtProvider` issues JWTs after successful authentication; controllers validate tokens via `JwtTokenValidator` filter.
- Services use Redis for caching frequently-read data and for storing ephemeral two-factor tokens / rate-limit counters.
- Payments flow to external providers (Stripe / Razorpay) and results are persisted to Postgres; large or async work can be pushed to Kafka for background processing.
