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

    Client["Client<br/>Browser / Mobile / API Consumer"] -->|HTTP JSON| RL["RateLimit<br/>Interceptor (Redis)"]

    subgraph App["Trading App - Spring Boot"]
        direction TB

        RL --> Controllers["Controllers<br/>AuthController, CryptoCurrencyController,<br/>OrderController, WalletController,<br/>PaymentController, ..."]

        Controllers --> Services["Services<br/>UserService, AuthService,<br/>PaymentService, TwoFactorOtpService, ..."]

        Services --> Repos["Repositories (JPA)<br/>UserRepository, OrderRepository, ..."]

        Services --> RedisCache["Redis Cache<br/>Session / Rate Limit"]
        Services --> Jwt["JwtProvider<br/>Token Generation"]
        Services --> EmailSrv["Email Service (SMTP)"]
        Services --> Kafka["Kafka Producer"]
    end

    Repos -->|JPA / SQL| Postgres[(Postgres Database)]
    RedisCache -->|fast reads/writes| Redis[(Redis)]
    EmailSrv -->|SMTP| Mail["SMTP / Mail Provider"]
    Controllers -->|REST / SDK| Stripe["Stripe API"]
    Controllers -->|REST / SDK| Razorpay["Razorpay API"]
    Kafka -->|publish| KafkaCluster[(Kafka Cluster)]
    KafkaCluster -->|consume| Workers["Background Workers<br/>Order Processors"]
    Workers --> Repos

    App -->|metrics / logs| Actuator["Spring Actuator<br/>Prometheus"]
    Docker["Docker / Docker Compose"] -.-> App

    classDef ext fill:#f9f,stroke:#333,stroke-width:1px;
    class Postgres,Redis,Stripe,Razorpay,Mail,KafkaCluster ext;
```
