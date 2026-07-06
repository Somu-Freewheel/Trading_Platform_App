# All about this service 

## How to run the service
First, make sure your Spring Boot application is running:
```bash
# If using Maven - The command to build the code 
cd "D:\download files\projects\trading_app"; .\mvnw.cmd clean package -DskipTests
cd 'D:\download files\projects\trading_website\trading_app\trading_app'; .\mvnw clean compile

# Docker containers up
docker-compose up --build
```
Your API should be available at: `http://localhost:8080`

---
## **Environment Setup**

Your `application.properties` shows:
- Server Port: **8080**
- Database: **MySQL** at `jdbc-mysql://mysql-db:3306/trading_app`
- Username: `root`
- Password: `user123`

Make sure MySQL is accessible before testing.

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

 