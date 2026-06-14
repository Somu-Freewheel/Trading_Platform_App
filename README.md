# All about this service 
## Service Overview
The Trading Platform App is a robust, Spring Boot-based RESTful service designed to facilitate secure cryptocurrency and asset trading operations. It leverages JWT authentication for secure API access, PostgreSQL for reliable data persistence, and Docker containerization for seamless deployment across multiple environments. The service supports comprehensive wallet management, real-time asset tracking, and transaction processing with role-based access control. Built with scalability and maintainability in mind, it provides a foundation for modern fintech applications with production-ready CI/CD pipeline integration through Jenkins.

## How to run the service
First, make sure your Spring Boot application is running:
```bash
# If using Maven - The command to build the code 
cd "D:\download files\projects\trading_website\trading_app\trading_app"; .\mvnw.cmd clean package -DskipTests
cd 'D:\download files\projects\trading_website\trading_app\trading_app'; .\mvnw clean compile

# Docker containers up
docker-compose up --build

# For Jenkins conatiner - Access Jenkins at http://localhost:8082
docker exec jenkins-container cat /var/jenkins_home/secrets/initialAdminPassword

```
Your API should be available at: `http://localhost:8080`

---
## **Environment Setup**

The application supports three environments: **DEV**, **STAGE**, and **PROD**.
### DEV Environment
| Service | Host Port | Container Port |
|----------|-----------|---------------|
| Trading App | 8080 | 8080 |
| PostgreSQL | 5432 | 5432 |
| PgAdmin | 8081 | 80 |

**Database Configuration**
```properties
spring.profiles.active=dev
spring.datasource.url=jdbc:postgresql://postgres-db:5432/trading_app_dev
spring.datasource.username=postgres
spring.datasource.password=<DEV_DB_PASSWORD>

```
### STG Environment
| Service | Host Port | Container Port |
|----------|-----------|---------------|
| Trading App | 8081      | 8080 |
| PostgreSQL | 5433      | 5432 |
| PgAdmin | 8083      | 80 |

**Database Configuration**
```properties
spring.profiles.active=stage
spring.datasource.url=jdbc:postgresql://postgres-db:5432/trading_app_stage
spring.datasource.username=postgres
spring.datasource.password=<STAGE_DB_PASSWORD>
```
### PROD Environment
| Service | Host Port | Container Port |
|----------|-----------|---------------|
| Trading App | 8082      | 8080 |
| PostgreSQL | 5434      | 5432 |
| PgAdmin | 8083      | 80 |

**Database Configuration**
```properties
spring.profiles.active=prod
spring.datasource.url=jdbc:postgresql://postgres-db:5432/trading_app_prod
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

## **Docker Compose Files**
- `docker-compose.yml`: For development environment
- `docker-compose.stage.yml`: For staging environment
- `docker-compose.prod.yml`: For production environment
````


# **Tips for Postman**

### **Save JWT Token in Postman Variables**
To avoid copying/pasting the token each time:

1. After login/signup, select the JWT token from response
2. Click on your request, go to **Tests** tab
3. Add this code:
```javascript
var jsonData = pm.response.json();
pm.environment.set("jwt_token", jsonData.jwt);
```

4. In Authorization headers, use: `Bearer {{jwt_token}}`

### **Create Postman Collection**
1. Save all requests in a collection for easy access
2. Order: Sign Up → Get Assets → Get Asset by Coin → Get Asset by ID

---

 