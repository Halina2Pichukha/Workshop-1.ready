# Currency Exchange Rates Provider Service

A full-stack application for managing currency exchange rates with Spring Boot backend and React frontend, featuring in-memory storage and mock exchange rate generation.

## 🚀 Features

### Backend (Spring Boot)
- **Currency Management**: Add and retrieve tracked currencies
- **Currency Conversion**: Convert amounts between supported currencies  
- **Exchange Rates Display**: View all currency pairs and exchange rates
- **In-Memory Storage**: All data stored in memory using ConcurrentHashMap
- **Mock Exchange Rates**: Random rate generation between 0.5 and 2.0
- **Bidirectional Rates**: If USD→EUR = X, then EUR→USD = 1/X
- **Role-Based Security**: ADMIN and USER roles with Spring Security
- **RESTful API**: Clean REST endpoints following best practices
- **Comprehensive Testing**: 31 unit and integration tests

### Frontend (React + Vite)
- **Currency List Display**: View all tracked currencies
- **Add Currency Form**: ADMIN-only feature with authentication
- **Currency Converter**: Convert between any tracked currencies
- **Exchange Rates Table**: Beautiful table showing all rates
- **Responsive Design**: Mobile, tablet, and desktop layouts
- **Real-time Updates**: Auto-refresh after adding currencies
- **Error Handling**: User-friendly error messages
- **Loading States**: Clear feedback during API calls

## 📋 Prerequisites

- **Java 21** - OpenJDK or Eclipse Temurin
- **Maven 3.6+** - For building the backend
- **Node.js 16+** and **npm** - For running the frontend

## 🛠️ Setup and Run

### Backend (Spring Boot)

#### 1. Set JAVA_HOME (Windows PowerShell)
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.x.x-hotspot"
```

#### 2. Build the project
```bash
.\mvnw.cmd clean install
```

#### 3. Run the application
```bash
.\mvnw.cmd spring-boot:run
```

The backend will start on `http://localhost:8080`

#### 4. Run tests
```bash
.\mvnw.cmd test
```

**Test Results**: ✅ 31 tests passed (0 failures)

### Frontend (React)

#### 1. Navigate to frontend directory
```bash
cd frontend
```

#### 2. Install dependencies
```bash
npm install
```

#### 3. Run development server
```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

#### 4. Build for production
```bash
npm run build
```

## 🔐 Authentication

The application uses in-memory authentication with the following credentials:

| Role | Username | Password | Access Level |
|------|----------|----------|--------------|
| ADMIN | `admin` | `admin123` | Full access (add currencies) |
| USER | `user` | `user123` | Read-only access |

**Note**: Only ADMIN can add new currencies. All GET endpoints are publicly accessible.

## 📡 API Endpoints

### Base URL: `/api/v1`

### 1. Get All Currencies
```http
GET /api/v1/currencies
```

**Response** (200 OK):
```json
{
  "currencies": ["USD", "EUR", "GBP"]
}
```

### 2. Add Currency (ADMIN only)
```http
POST /api/v1/currencies?currency=USD
Authorization: Basic admin:admin123
```

**Responses**:
- 201 Created: Currency added successfully
- 400 Bad Request: Empty currency code
- 403 Forbidden: Non-ADMIN user

### 3. Convert Currency
```http
GET /api/v1/exchange?amount=100&from=USD&to=EUR
```

**Response** (200 OK):
```json
{
  "amount": 100.0,
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "convertedAmount": 150.0,
  "rate": 1.5
}
```

**Error Responses**:
- 400 Bad Request: Negative amount
- 404 Not Found: Unsupported currency

## 🧪 Testing with cURL

### Get currencies (initially empty)
```bash
curl http://localhost:8080/api/v1/currencies
```

### Add currency as ADMIN
```bash
curl -X POST -u admin:admin123 "http://localhost:8080/api/v1/currencies?currency=USD"
curl -X POST -u admin:admin123 "http://localhost:8080/api/v1/currencies?currency=EUR"
```

### Try to add currency as USER (should fail with 403)
```bash
curl -X POST -u user:user123 "http://localhost:8080/api/v1/currencies?currency=GBP"
```

### Convert currency
```bash
curl "http://localhost:8080/api/v1/exchange?amount=100&from=USD&to=EUR"
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/example/currencyexchange/
│   │   ├── controller/         # REST controllers
│   │   ├── service/            # Business logic
│   │   ├── model/              # Domain models
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── exception/          # Custom exceptions
│   │   └── security/           # Security configuration
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/currencyexchange/
        ├── service/            # Service unit tests
        └── controller/         # Controller integration tests
```

## 📝 Implementation Details

### In-Memory Storage
- Uses `ConcurrentHashMap` for thread-safe operations
- Data persists only during application runtime
- Empty state on startup

### Exchange Rate Generation
- Random rates between 0.5 and 2.0
- Bidirectional consistency: if USD→EUR = 1.5, then EUR→USD = 0.667
- Rates generated when new currency is added

### Security
- Spring Security with HTTP Basic Authentication
- Method-level security with `@PreAuthorize`
- CORS enabled for React frontend integration

## 🎯 Acceptance Criteria Status

✅ Currency Management
- ✅ Retrieve list of tracked currencies
- ✅ Add currency as ADMIN
- ✅ Block add currency as non-ADMIN
- ✅ Validate currency input

✅ Currency Conversion
- ✅ Convert between tracked currencies
- ✅ Reject negative amounts
- ✅ Handle unsupported currencies

✅ System Behavior
- ✅ Initial empty currency list
- ✅ In-memory storage
- ✅ No external API calls
- ✅ Clear error messages

## 🔜 Next Steps (Phase 2)

See `TODO.md` for the complete implementation checklist.

Phase 2 will include:
- Enhanced validation
- Additional testing
- React frontend development

## 📄 License

This is a workshop project for educational purposes.
