# Asset API Testing Guide in Postman

## Overview
The Asset APIs in your application require JWT authentication. You have 3 endpoints to test:
1. `GET /api/asset/{assetId}` - Get asset by ID
2. `GET /api/asset/coin/{coinId}/user` - Get asset by coin ID for logged-in user
3. `GET /api/asset` - Get all assets for logged-in user

---

## Step-by-Step Testing Instructions

### **STEP 1: Start Your Application**

First, make sure your Spring Boot application is running:

```bash
# If using Maven
mvn spring-boot:run

# Or if using Docker
docker-compose up --build
```

Your API should be available at: `http://localhost:8080`

---

### **STEP 2: Create a User Account (Sign Up)**

1. Open **Postman**
2. Create a new **POST** request
3. **URL:** `http://localhost:8080/auth/signup`
4. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

5. **Body** (raw JSON):
```json
{
  "email": "testuser@example.com",
  "password": "password123",
  "fullName": "Test User"
}
```

6. Click **Send**
7. **Save the JWT token** from the response (you'll need it):
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "status": true,
  "message": "Register Successful"
}
```

---

### **STEP 3: Login (If you already have an account)**

If you already created an account, login instead:

1. Create a **POST** request
2. **URL:** `http://localhost:8080/auth/signin`
3. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

4. **Body** (raw JSON):
```json
{
  "email": "testuser@example.com",
  "password": "password123"
}
```

5. Click **Send**
6. **Save the JWT token** from the response

---

### **STEP 4: Test Asset Endpoint 1 - Get All Assets**

1. Create a **GET** request
2. **URL:** `http://localhost:8080/api/asset`
3. **Headers:**
   - Key: `Authorization`
   - Value: `Bearer <YOUR_JWT_TOKEN>` (replace with actual token from Step 2/3)
   - Key: `Content-Type`
   - Value: `application/json`

4. Click **Send**
5. **Expected Response:**
   - Status: `200 OK`
   - Body: List of assets for the user
   ```json
   [
     {
       "id": 1,
       "coinId": "bitcoin",
       "quantity": 0.5,
       "userId": 1,
       ...
     }
   ]
   ```

---

### **STEP 5: Test Asset Endpoint 2 - Get Asset by Coin ID**

1. Create a **GET** request
2. **URL:** `http://localhost:8080/api/asset/coin/bitcoin/user` (replace "bitcoin" with actual coin ID)
3. **Headers:**
   - Key: `Authorization`
   - Value: `Bearer <YOUR_JWT_TOKEN>`
   - Key: `Content-Type`
   - Value: `application/json`

4. Click **Send**
5. **Expected Response:**
   - Status: `200 OK`
   - Body: Single asset object
   ```json
   {
     "id": 1,
     "coinId": "bitcoin",
     "quantity": 0.5,
     "userId": 1,
     ...
   }
   ```

---

### **STEP 6: Test Asset Endpoint 3 - Get Asset by Asset ID**

1. Create a **GET** request
2. **URL:** `http://localhost:8080/api/asset/1` (replace "1" with actual asset ID from Step 4 or 5)
3. **Headers:**
   - Key: `Authorization`
   - Value: `Bearer <YOUR_JWT_TOKEN>`
   - Key: `Content-Type`
   - Value: `application/json`

4. Click **Send**
5. **Expected Response:**
   - Status: `200 OK`
   - Body: Asset object
   ```json
   {
     "id": 1,
     "coinId": "bitcoin",
     "quantity": 0.5,
     "userId": 1,
     ...
   }
   ```

---

## **Common Issues & Solutions**

### **Issue 1: 403 Forbidden Error**
```json
{
  "timestamp": "2026-02-16T17:21:22.132+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/asset/1"
}
```

**Solution:**
- You forgot to add the JWT token in the Authorization header
- Make sure you add the token in format: `Bearer <token>`
- Token might have expired - get a new one by signing in again

### **Issue 2: Invalid Token Error**
```
java.lang.RuntimeException: invalid token...
```

**Solution:**
- Copy the exact JWT token from the login/signup response
- Make sure there are no extra spaces or characters
- Try getting a new token

### **Issue 3: Missing Path Variable Error**
```
Required URI template variable 'assetId' for method parameter type Long is not present
```

**Solution:**
- For endpoint `/api/asset/{assetId}`, replace `{assetId}` with an actual number (e.g., `/api/asset/1`)
- For endpoint `/api/asset/coin/{coinId}/user`, replace `{coinId}` with an actual coin ID

### **Issue 4: 401 Unauthorized**
- JWT token has expired or is invalid
- Get a new token from `/auth/signin`

---

## **Tips for Postman**

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

## **Expected Workflow**
```
1. Sign Up → Get JWT Token
2. Get All Assets (GET /api/asset)
3. Note an asset ID or coin ID
4. Get Asset by Coin ID (GET /api/asset/coin/{coinId}/user)
5. Get Asset by ID (GET /api/asset/{assetId})
```

---

## **Database Requirements**

Make sure your MySQL database is running and has:
- User table with test user data
- Asset table with test asset data

If you're using Docker, ensure docker-compose.yml has MySQL service running:
```bash
docker-compose up -d mysql-db
```

---

## **Environment Setup**

Your `application.properties` shows:
- Server Port: **8080**
- Database: **MySQL** at `jdbc-mysql://mysql-db:3306/trading_app`
- Username: `root`
- Password: `user123`

Make sure MySQL is accessible before testing.

