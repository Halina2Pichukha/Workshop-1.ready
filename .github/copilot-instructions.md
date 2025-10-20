# Currency Exchange Rates Provider Service - AI Agent Instructions

## Project Overview
This is a workshop project to build a **Currency Exchange Rates Provider Service** with Spring Boot backend (Java 21) and React frontend. The system uses **in-memory storage** (no database) and generates **mock exchange rates** (no external API calls).

## Architecture Principles

### Core Constraint: In-Memory Everything
- Exchange rates are generated randomly and stored in `ConcurrentHashMap`
- Rates must be bidirectional and consistent: if USD→EUR = 1.5, then EUR→USD = 0.667
- Data persists only during application runtime (lost on restart)
- Initial state: empty currency list
- No external API integration or database connections

### API Contract (REST)
Base path: `/api/v1`

**Currency Management:**
- `GET /currencies` → List all tracked currencies (200 OK, always succeeds)
- `POST /currencies?currency={CODE}` → Add currency (ADMIN only)
  - 201 Created: Success, generates rates vs all existing currencies
  - 400 Bad Request: Empty/invalid currency code
  - 403 Forbidden: Non-ADMIN user

**Currency Conversion:**
- `GET /exchange?amount={num}&from={CODE}&to={CODE}` → Convert currency
  - 200 OK: Valid conversion
  - 400 Bad Request: Negative amount
  - 404 Not Found: Unsupported currency

### Security Model
- Use Spring Security with in-memory authentication
- Two roles: `ADMIN` (can add currencies), `USER` (read-only)
- Only POST `/api/v1/currencies` requires ADMIN
- GET endpoints are public or basic auth (configurable)

## Implementation Guidelines

### Backend (Spring Boot)
**Project Structure:**
```
src/main/java/com/example/currencyexchange/
├── controller/      # REST controllers (@RestController)
├── service/         # Business logic, exchange rate generation
├── model/           # Currency, ExchangeRate POJOs
├── dto/             # Response DTOs (no request DTOs - use @RequestParam)
├── exception/       # Custom exceptions + @ControllerAdvice
└── security/        # Security configuration
```

**Key Patterns:**
- Use `@ControllerAdvice` for global exception handling (400/403/404 mappings)
- Exchange rate service must ensure bidirectional consistency
- Mock rate generation: random value between 0.5 and 2.0
- Thread-safe in-memory storage: `ConcurrentHashMap<String, Currency>`

**Dependencies (pom.xml):**
- Java 21 (target version)
- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-test` (with JUnit 5)

### Frontend (React)
**Project Structure:**
```
src/
├── components/      # UI components
├── services/        # API client (axios/fetch wrapper)
└── App.js           # Main component
```

**Key Components:**
- `CurrencyList`: Display tracked currencies (fetch on mount)
- `AddCurrency`: Form with ADMIN auth (input + submit button)
- `CurrencyConverter`: Amount input + from/to dropdowns + convert button
- `ExchangeRateDisplay`: Show all currency pairs and rates

**API Service Layer:**
- Create centralized API client in `services/api.js`
- Handle auth headers for ADMIN operations
- Parse and display error messages from backend

## Development Workflow

### Backend Testing
Run tests with: `./mvnw test` (Maven) or `./gradlew test` (Gradle)

**Test Coverage Checklist:**
- Empty currency list on startup
- Add currency (ADMIN success, non-ADMIN 403)
- Validation (empty currency → 400)
- Conversion (valid → 200, negative amount → 400, missing currency → 404)
- In-memory persistence across multiple requests

### Frontend Testing
Use Jest + React Testing Library for component tests

**Critical Scenarios:**
- Display empty state (no currencies)
- Handle API errors (show user-friendly messages)
- ADMIN-only features disabled for non-ADMIN users

### Integration Testing
- Configure CORS in Spring Boot to allow React origin
- Test full flow: React UI → Backend API → Response
- Verify no external network calls (mock all dependencies)

## Acceptance Criteria Compliance
Reference `currency_exchange_acceptance_criteria.md` for all requirements. Every scenario must pass:
- ✅ Currency management (GET/POST with auth)
- ✅ Currency conversion (valid/invalid inputs)
- ✅ System behavior (empty start, in-memory storage, error handling)
- ✅ UI interaction (display rates, add currency)

## Common Pitfalls
1. **Bidirectional rates:** Don't store USD→EUR without EUR→USD
2. **Empty validation:** Check for empty strings, not just null
3. **Status codes:** Use correct HTTP codes (400 vs 404 vs 403)
4. **CORS:** Enable in Spring Boot or React proxy won't work
5. **In-memory loss:** Don't implement persistence - it's intentional

## Quick Start Commands
```bash
# Backend
./mvnw spring-boot:run          # Start Spring Boot
./mvnw test                     # Run tests

# Frontend (after React project created)
npm install                     # Install dependencies
npm start                       # Start dev server
npm test                        # Run tests
```

## Reference Files
- `currency_exchange_acceptance_criteria.md` - Complete functional requirements
- `TODO.md` - Implementation checklist with priority order

---
**Workshop Context:** This is a learning exercise focusing on REST API design, Spring Security, in-memory state management, and full-stack integration. Prioritize acceptance criteria compliance over production-ready features.
