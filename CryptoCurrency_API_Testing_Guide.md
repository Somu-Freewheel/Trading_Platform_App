# Cryptocurrency API Testing Guide in Postman

## Overview
The CryptoCurrency APIs interact with the **CoinGecko API** to fetch cryptocurrency data. These endpoints **DO NOT require JWT authentication** - they are public endpoints. All APIs return JSON data about cryptocurrencies.

---

## CryptoCurrency Endpoints Summary

| Method | Endpoint | Parameters | Description |
|--------|----------|-----------|-------------|
| GET | `/coins` | `page` (required) | Get paginated list of cryptocurrencies (10 per page) |
| GET | `/coins/{coinId}/chart` | `coinId`, `days` | Get market chart data for a specific coin |
| GET | `/coins/search` | `q` (query) | Search for a coin by keyword |
| GET | `/coins/top50` | None | Get top 50 cryptocurrencies by market cap |
| GET | `/coins/trading` | None | Get trading coins list |
| GET | `/coins/details/{coinId}` | `coinId` | Get detailed information about a coin |

---

## Step-by-Step Testing Instructions

### **STEP 1: Start Your Application**

Make sure your Spring Boot application is running:

```bash
# Using Maven
mvn spring-boot:run

# Or using Docker
docker-compose up --build
```

Your API will be available at: `http://localhost:8080`

---

### **STEP 2: Test Endpoint 1 - Get Coin List (Paginated)**

**Purpose:** Fetch a paginated list of 10 cryptocurrencies

1. Open **Postman**
2. Create a new **GET** request
3. **URL:** `http://localhost:8080/coins?page=1`
4. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

5. **Query Parameters:**
   - Key: `page`
   - Value: `1` (use `2`, `3`, etc. for other pages)

6. Click **Send**
7. **Expected Response:** Status `202 Accepted`
```json
[
  {
    "id": "bitcoin",
    "symbol": "btc",
    "name": "Bitcoin",
    "image": "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
    "currentPrice": 45000.50,
    "marketCap": 950000000000,
    "marketCapRank": 1,
    "priceChangePercentage24h": 2.5
  },
  {
    "id": "ethereum",
    "symbol": "eth",
    "name": "Ethereum",
    "image": "https://assets.coingecko.com/coins/images/279/large/ethereum.png",
    "currentPrice": 2500.75,
    "marketCap": 300000000000,
    "marketCapRank": 2,
    "priceChangePercentage24h": 1.8
  }
  // ... more coins
]
```

**Tips:**
- Try different page numbers: `page=1`, `page=2`, `page=3`, etc.
- Each page returns 10 coins

---

### **STEP 3: Test Endpoint 2 - Get Market Chart Data**

**Purpose:** Get historical price and market data for a specific coin

1. Create a new **GET** request
2. **URL:** `http://localhost:8080/coins/bitcoin/chart?days=7`
3. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

4. **Path Parameters:**
   - Replace `{coinId}` with actual coin ID (e.g., `bitcoin`, `ethereum`, `cardano`)

5. **Query Parameters:**
   - Key: `days`
   - Value: `7` (you can use 1, 7, 30, 90, 365, etc.)

6. Click **Send**
7. **Expected Response:** Status `202 Accepted`
```json
{
  "prices": [
    [1610000000000, 35000.50],
    [1610086400000, 35500.75],
    [1610172800000, 36000.25]
    // ... more price points
  ],
  "market_caps": [
    [1610000000000, 650000000000],
    [1610086400000, 660000000000]
    // ... more data
  ],
  "volumes": [
    [1610000000000, 25000000000],
    [1610086400000, 27000000000]
    // ... more data
  ]
}
```

**Common Coin IDs to Test:**
- `bitcoin`
- `ethereum`
- `cardano`
- `solana`
- `ripple`
- `polkadot`

**Days Parameter Options:**
- `1` - Last 24 hours
- `7` - Last 7 days
- `30` - Last 30 days
- `90` - Last 90 days
- `365` - Last year

---

### **STEP 4: Test Endpoint 3 - Search for a Coin**

**Purpose:** Search for cryptocurrencies by keyword

1. Create a new **GET** request
2. **URL:** `http://localhost:8080/coins/search?q=bitcoin`
3. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

4. **Query Parameters:**
   - Key: `q`
   - Value: `bitcoin` (or any keyword like `eth`, `doge`, `shibu`, etc.)

5. Click **Send**
6. **Expected Response:** Status `200 OK`
```json
{
  "coins": [
    {
      "id": "bitcoin",
      "name": "Bitcoin",
      "symbol": "btc",
      "market_cap_rank": 1,
      "thumb": "https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png",
      "large": "https://assets.coingecko.com/coins/images/1/large/bitcoin.png"
    }
    // ... more search results
  ]
}
```

**Test Different Keywords:**
- `bitcoin`
- `ethereum`
- `doge`
- `shiba`

---

### **STEP 5: Test Endpoint 4 - Get Top 50 Cryptocurrencies**

**Purpose:** Get the top 50 cryptocurrencies ranked by market cap

1. Create a new **GET** request
2. **URL:** `http://localhost:8080/coins/top50`
3. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

4. **No parameters needed**
5. Click **Send**
6. **Expected Response:** Status `200 OK`
```json
[
  {
    "id": "bitcoin",
    "symbol": "btc",
    "name": "Bitcoin",
    "current_price": 45000.50,
    "market_cap": 950000000000,
    "market_cap_rank": 1,
    "image": "https://assets.coingecko.com/coins/images/1/large/bitcoin.png"
  },
  {
    "id": "ethereum",
    "symbol": "eth",
    "name": "Ethereum",
    "current_price": 2500.75,
    "market_cap": 300000000000,
    "market_cap_rank": 2,
    "image": "https://assets.coingecko.com/coins/images/279/large/ethereum.png"
  }
  // ... top 50 coins
]
```

---

### **STEP 6: Test Endpoint 5 - Get Trading Coins**

**Purpose:** Get a list of trading cryptocurrencies

1. Create a new **GET** request
2. **URL:** `http://localhost:8080/coins/trading`
3. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

4. **No parameters needed**
5. Click **Send**
6. **Expected Response:** Status `200 OK`
```json
{
  "coins": [
    {
      "id": "bitcoin",
      "name": "Bitcoin",
      "symbol": "btc",
      "market_cap_rank": 1,
      "thumb": "https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png"
    },
    {
      "id": "ethereum",
      "name": "Ethereum",
      "symbol": "eth",
      "market_cap_rank": 2,
      "thumb": "https://assets.coingecko.com/coins/images/279/thumb/ethereum.png"
    }
    // ... more trading coins
  ]
}
```

---

### **STEP 7: Test Endpoint 6 - Get Coin Details**

**Purpose:** Get detailed information about a specific cryptocurrency

1. Create a new **GET** request
2. **URL:** `http://localhost:8080/coins/details/bitcoin`
3. **Headers:**
   - Key: `Content-Type`
   - Value: `application/json`

4. **Path Parameters:**
   - Replace `{coinId}` with actual coin ID (e.g., `bitcoin`, `ethereum`)

5. Click **Send**
6. **Expected Response:** Status `200 OK`
```json
{
  "id": "bitcoin",
  "symbol": "btc",
  "name": "Bitcoin",
  "image": "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
  "currentPrice": 45000.50,
  "marketCap": 950000000000,
  "marketCapRank": 1,
  "description": "Bitcoin is the first and most well-known cryptocurrency...",
  "links": {
    "homepage": ["https://bitcoin.org/"],
    "github": ["https://github.com/bitcoin/bitcoin"]
  }
}
```

**Test Multiple Coins:**
- `bitcoin`
- `ethereum`
- `cardano`
- `solana`

---

## Common Issues & Solutions

### **Issue 1: 404 Not Found**
```json
{
  "timestamp": "2026-02-16T18:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/coins/invalid-coin/chart"
}
```

**Solution:**
- Check the coin ID spelling (should be lowercase, use hyphen for spaces)
- Use valid coin IDs like `bitcoin`, `ethereum`, `cardano`
- Make sure the URL path is correct

### **Issue 2: Missing Query Parameter**
```
Required String parameter 'page' is not present
```

**Solution:**
- For `/coins` endpoint, you MUST provide `?page=1`
- For `/coins/{coinId}/chart`, you MUST provide `?days=7`
- For `/coins/search`, you MUST provide `?q=keyword`

### **Issue 3: External API Timeout**
```
Error fetching coin list: Connection timeout
```

**Solution:**
- Check your internet connection
- Wait a moment and retry (CoinGecko API might be temporarily slow)
- Check if CoinGecko API is down (https://www.coingecko.com/)

### **Issue 4: 500 Internal Server Error**
```json
{
  "timestamp": "2026-02-16T18:00:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error"
}
```

**Solution:**
- Check application logs for detailed error message
- Verify the coin ID is valid
- Ensure parameters are in correct format

---

## Complete Testing Workflow

### **Quick Test Sequence (No Authentication Needed):**

1. **Get Coin List** → `http://localhost:8080/coins?page=1`
2. **Get Top 50** → `http://localhost:8080/coins/top50`
3. **Get Trading Coins** → `http://localhost:8080/coins/trading`
4. **Get Bitcoin Details** → `http://localhost:8080/coins/details/bitcoin`
5. **Get Bitcoin Chart** → `http://localhost:8080/coins/bitcoin/chart?days=7`
6. **Search for Ethereum** → `http://localhost:8080/coins/search?q=ethereum`

---

## Postman Collection Setup

### **Create Environment Variables (Optional)**

1. In Postman, go to **Manage Environments**
2. Create a new environment `Trading App`
3. Add variable:
   - Variable: `base_url`
   - Value: `http://localhost:8080`

4. Use in URLs: `{{base_url}}/coins?page=1`

### **Organize Requests**

Create a Postman collection with folders:
```
Trading App - Crypto APIs
├── Get Coin List
├── Get Market Chart
├── Search Coin
├── Get Top 50
├── Get Trading Coins
└── Get Coin Details
```

---

## Important Notes

✅ **These endpoints DO NOT require JWT authentication**
✅ **All endpoints interact with external CoinGecko API**
✅ **Responses use HTTP Status `200 OK` or `202 Accepted`**
✅ **Internet connection required to fetch external data**
✅ **Rate limiting applies from CoinGecko API**

---

## Valid Coin IDs (Sample List)

| Coin ID | Name | Symbol |
|---------|------|--------|
| bitcoin | Bitcoin | BTC |
| ethereum | Ethereum | ETH |
| cardano | Cardano | ADA |
| solana | Solana | SOL |
| ripple | Ripple | XRP |
| polkadot | Polkadot | DOT |
| dogecoin | Dogecoin | DOGE |
| litecoin | Litecoin | LTC |
| stellar | Stellar | XLM |
| chainlink | Chainlink | LINK |

You can find more at: https://api.coingecko.com/api/v3/coins/list

