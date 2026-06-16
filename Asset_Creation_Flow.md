# Asset Creation Flow - Complete Analysis

## Overview
Assets in your trading application are **NOT created directly** via the AssetController. Instead, they are **created automatically when users buy cryptocurrency** through an order placement. Here's the complete flow:

---

## Architecture Summary

### Key Entities
1. **Asset** - Represents user's holdings of a specific cryptocurrency
   - `id` (Long) - Primary key
   - `quantity` (double) - Amount of crypto held
   - `buyPrice` (double) - Average purchase price
   - `user` (User) - ManyToOne relationship
   - `coin` (Cryptocurrency) - ManyToOne relationship

2. **Order** - Represents a buy/sell transaction
   - `orderType` - BUY or SELL
   - `orderStatus` - PENDING, SUCCESS, etc.
   - `price` - Total transaction amount
   - `user` - Owner of the order
   - `orderItem` - Contains coin and quantity details

3. **OrderItem** - Details of what's being traded
   - `coin` - Cryptocurrency being traded
   - `quantity` - Amount of crypto
   - `buyPrice` - Purchase price (for BUY orders)
   - `sellPrice` - Sale price (for SELL orders)

---

## Step-by-Step Asset Creation Flow

### **Step 1: User Authentication**
- Get JWT token via sign up/login endpoints
- All subsequent requests require `Authorization: Bearer {jwt_token}` header

### **Step 2: User Initiates a BUY Order**
**Endpoint:** `POST /pay`
**Request Body:**
```json
{
  "coinId": "bitcoin",
  "quantity": 0.5,
  "orderType": "BUY"
}
```

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

### **Step 3: OrderController Processes the Request**
```
OrderController.payOrderPayment()
├── Extract user from JWT token
├── Find cryptocurrency by coinId
└── Call orderService.processOrder()
```

### **Step 4: OrderService.processOrder() Routes to buyAsset()**
```
OrderService.processOrder(coin, quantity, OrderType.BUY, user)
├── Check OrderType is BUY
└── Call buyAsset(coin, quantity, user)
```

### **Step 5: OrderServiceImpl.buyAsset() - The Asset Creation Logic**

#### **5.1 Validate Request**
- Check quantity > 0
- If not, throw RuntimeException

#### **5.2 Create OrderItem**
```java
OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
// Sets: coin, quantity, buyPrice (current price), sellPrice (0)
// Saves to database
```

#### **5.3 Create Order**
```java
Order order = createOrder(user, orderItem, OrderType.BUY);
// Sets: user, orderItem, orderType, price, timestamp, status (PENDING)
// Saves to database
```

#### **5.4 Process Wallet Payment**
```java
walletService.payOrderPayment(order, user);
// Deducts amount from user's wallet
```

#### **5.5 Update Order Status**
```java
order.setOrderStatus(OrderStatus.SUCCESS);
Order savedOrder = orderRepository.save(order);
```

#### **5.6 CREATE THE ASSET ✅ (This is where assets are created)**
```java
Asset oldAsset = assetService.findAssetByUserIdAndCoinId(
    order.getUser().getId(),
    order.getOrderItem().getCoin().getId()
);

if(oldAsset == null) {
    // NEW ASSET - First time user is buying this coin
    assetService.creatAsset(user, orderItem.getCoin(), orderItem.getQuantity());
} else {
    // EXISTING ASSET - User already owns this coin
    // Update quantity by adding new quantity
    assetService.updatedAsset(oldAsset.getId(), oldAsset.getQuantity());
}
```

### **Step 6: AssetService.creatAsset() - Final Asset Creation**
```java
public Asset creatAsset(User user, Cryptocurrency coin, double quantity) {
    Asset asset = new Asset();
    asset.setUser(user);                              // Link to user
    asset.setCoin(coin);                              // Link to cryptocurrency
    asset.setQuantity(quantity);                      // Initial quantity
    asset.setBuyPrice(coin.getCurrentPrice());        // Current price at purchase
    return assetRepository.save(asset);               // Save to database
}
```

---

## Complete Request/Response Flow

### **Request Flow:**
```
Client (Postman)
    ↓
POST /pay with JWT & CreateOrderRequest
    ↓
OrderController.payOrderPayment()
    ↓
OrderService.processOrder()
    ↓
OrderService.buyAsset()
    ├─→ Create OrderItem
    ├─→ Create Order
    ├─→ Process Payment (Wallet)
    ├─→ Check if Asset exists
    └─→ Create New Asset OR Update Existing Asset
    ↓
AssetRepository.save(asset)
    ↓
Response: Order object with status SUCCESS
```

### **Response Example:**
```json
{
  "id": 1,
  "user": { "id": 1, "email": "user@example.com" },
  "orderItem": {
    "coin": {
      "id": "bitcoin",
      "name": "Bitcoin",
      "currentPrice": 45000
    },
    "quantity": 0.5,
    "buyPrice": 45000
  },
  "orderType": "BUY",
  "orderStatus": "SUCCESS",
  "price": 22500,
  "timestamp": "2026-03-14T10:30:00"
}
```

---

## Key Asset Methods in AssetController

### 1. **Get All Assets for User**
```
GET /api/asset
Headers: Authorization: Bearer {jwt}
Response: List of all assets owned by the user
```

### 2. **Get Asset by Asset ID**
```
GET /api/asset/{assetId}
Headers: Authorization: Bearer {jwt}
Response: Single Asset object
```

### 3. **Get Asset by Cryptocurrency**
```
GET /api/asset/coin/{coinId}/user
Headers: Authorization: Bearer {jwt}
Response: Asset of the specified coin for this user
```

---

## Database Relationships

```
User (1) ──→ (Many) Asset ←── (Many) Cryptocurrency
              ↑
              │ owns
              │
        Current Holdings
        
Order (1) ──→ (1) OrderItem ←── Cryptocurrency
 ↓
User
```

---

## Important Notes

1. **Assets are created AUTOMATICALLY** when users buy crypto, not through direct API calls
2. **First purchase creates a new Asset**, subsequent purchases of the same coin update quantity
3. **buyPrice is locked** at the current price when the order is created
4. **Wallet balance is deducted** before the asset is created
5. **Order must be SUCCESS** before an asset is created
6. **SELL orders decrease asset quantity** or delete the asset if quantity drops below threshold

---

## Complete BUY Process Summary

```
1. User calls POST /pay with BUY request
2. ↓
3. Validate quantity > 0
4. ↓
5. Get current cryptocurrency price
6. ↓
7. Create OrderItem with coin, quantity, buyPrice
8. ↓
9. Create Order (status: PENDING)
10. ↓
11. Deduct payment from wallet
12. ↓
13. Set order status to SUCCESS
14. ↓
15. Check if user already owns this coin (Asset exists?)
16. ↓
17. If NEW: Create Asset with quantity, buyPrice, coin, user
18. If EXISTS: Update Asset by adding new quantity
19. ↓
20. Return Order object with SUCCESS status
21. ↓
22. NEW ASSET IS NOW CREATED & STORED IN DATABASE ✅
```

---

## Testing in Postman

### **Prerequisites:**
1. Sign up/Login and get JWT token
2. Have cryptocurrency data in database

### **Request:**
```
POST http://localhost:8080/pay

Headers:
- Authorization: Bearer eyJhbGc...
- Content-Type: application/json

Body:
{
  "coinId": "bitcoin",
  "quantity": 1.5,
  "orderType": "BUY"
}
```

### **Expected Response (201 or 200):**
```json
{
  "id": 123,
  "orderStatus": "SUCCESS",
  "orderType": "BUY",
  ...
}
```

### **Verify Asset Created:**
```
GET http://localhost:8080/api/asset

Headers:
- Authorization: Bearer eyJhbGc...

Response: Array of assets created from purchases
```

---

## Flow Diagram

```
┌─────────────────────────────────────────────────────────┐
│              USER PLACES BUY ORDER                      │
│     POST /pay with coinId, quantity, OrderType.BUY     │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
        ┌──────────────────────────┐
        │  OrderController         │
        │  payOrderPayment()       │
        └─────────┬────────────────┘
                  │
                  ▼
       ┌──────────────────────────────┐
       │   OrderService              │
       │   processOrder()            │
       │   → buyAsset()              │
       └─────────┬────────────────────┘
                 │
        ┌────────┴──────────────┐
        │                       │
        ▼                       ▼
   ┌─────────────┐      ┌──────────────┐
   │ Create      │      │ Create       │
   │ OrderItem   │      │ Order        │
   └─────┬───────┘      └──────┬───────┘
         │                     │
         └─────────┬───────────┘
                   │
                   ▼
         ┌──────────────────┐
         │ Process Wallet   │
         │ Payment          │
         └────────┬─────────┘
                  │
                  ▼
        ┌──────────────────────┐
        │ Check if Asset       │
        │ already exists       │
        └────────┬─────────────┘
                 │
        ┌────────┴─────────┐
        │ NO              │ YES
        ▼                 ▼
   ┌──────────────┐  ┌──────────────┐
   │ CREATE NEW   │  │ UPDATE       │
   │ ASSET        │  │ EXISTING     │
   │              │  │ ASSET        │
   │ creatAsset() │  │              │
   │              │  │ updatedAsset()
   └──────┬───────┘  └──────┬───────┘
          │                 │
          └────────┬────────┘
                   │
                   ▼
        ┌──────────────────────┐
        │ Save to Database     │
        │ assetRepository.     │
        │ save(asset)          │
        └────────┬─────────────┘
                 │
                 ▼
        ┌──────────────────────┐
        │ Return Order         │
        │ with status SUCCESS  │
        │ ASSET NOW CREATED ✅ │
        └──────────────────────┘
```

