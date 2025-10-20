# Currency Exchange Rates Provider Service - TODO List

## 📋 Project Overview
Implementation checklist for building a Currency Exchange Rates Provider Service with Spring Boot backend and React frontend.

---

## 🏗️ Backend Implementation (Spring Boot)

### 1. Project Setup
- [ ] Initialize Spring Boot project with required dependencies
  - [ ] Spring Web
  - [ ] Spring Security
  - [ ] Spring Boot DevTools
  - [ ] Lombok (optional)
- [ ] Configure application.properties/yml
- [ ] Set up project structure (controllers, services, models, repositories)

### 2. Data Models & DTOs
- [ ] Create Currency model/entity
- [ ] Create ExchangeRate model to store currency pairs and rates
- [ ] Create DTOs for responses:
  - [ ] CurrencyListResponse
  - [ ] ConversionResponse
  - [ ] ErrorResponse

### 3. In-Memory Storage
- [ ] Implement in-memory storage for currencies (e.g., ConcurrentHashMap)
- [ ] Implement in-memory storage for exchange rates
- [ ] Create service to manage currency data
- [ ] Create service to manage exchange rate data

### 4. Currency Management API (`/api/v1/currencies`)
- [ ] **GET** `/api/v1/currencies` - Retrieve tracked currencies
  - [ ] Return empty list on fresh start
  - [ ] Return 200 OK status
  - [ ] Return list of all tracked currencies
- [ ] **POST** `/api/v1/currencies?currency={code}` - Add new currency
  - [ ] Validate currency parameter (not empty)
  - [ ] Return 400 Bad Request if currency is empty
  - [ ] Check user ADMIN privileges
  - [ ] Return 403 Forbidden for non-ADMIN users
  - [ ] Add currency to tracked list
  - [ ] Generate mock exchange rates for new currency against all existing currencies
  - [ ] Return 201 Created status on success

### 5. Currency Conversion API (`/api/v1/exchange`)
- [ ] **GET** `/api/v1/exchange?amount={amount}&from={from}&to={to}` - Convert currency
  - [ ] Validate amount parameter (must be positive)
  - [ ] Return 400 Bad Request if amount is negative or invalid
  - [ ] Check if source currency is tracked
  - [ ] Check if target currency is tracked
  - [ ] Return 404 Not Found if either currency is not tracked
  - [ ] Retrieve exchange rate from in-memory storage
  - [ ] Calculate converted amount
  - [ ] Return 200 OK with converted amount

### 6. Exchange Rate Service
- [ ] Implement mock exchange rate generator
- [ ] Create method to generate random rates (e.g., between 0.5 and 2.0)
- [ ] Store rates in memory (bidirectional: USD->EUR and EUR->USD)
- [ ] Ensure consistency: if USD->EUR = X, then EUR->USD = 1/X
- [ ] Method to retrieve rate for currency pair

### 7. Security Configuration
- [ ] Configure Spring Security
- [ ] Create ADMIN role
- [ ] Create USER role (or default role)
- [ ] Protect POST `/api/v1/currencies` with ADMIN role
- [ ] Allow public access to GET endpoints (or configure as needed)
- [ ] Set up in-memory authentication for testing (username/password)

### 8. Error Handling
- [ ] Create global exception handler (@ControllerAdvice)
- [ ] Handle validation errors (400 Bad Request)
- [ ] Handle authorization errors (403 Forbidden)
- [ ] Handle not found errors (404 Not Found)
- [ ] Return clear error messages in response body
- [ ] Include appropriate HTTP status codes

### 9. Testing (Backend)
- [ ] Unit tests for Currency service
- [ ] Unit tests for Exchange rate service
- [ ] Integration tests for `/api/v1/currencies` GET endpoint
- [ ] Integration tests for `/api/v1/currencies` POST endpoint (ADMIN)
- [ ] Integration tests for `/api/v1/currencies` POST endpoint (non-ADMIN)
- [ ] Integration tests for `/api/v1/exchange` endpoint (valid request)
- [ ] Integration tests for `/api/v1/exchange` endpoint (negative amount)
- [ ] Integration tests for `/api/v1/exchange` endpoint (unsupported currency)
- [ ] Test initial state (empty currency list)
- [ ] Test in-memory persistence across multiple requests

---

## 🎨 Frontend Implementation (React)

### 1. Project Setup
- [ ] Initialize React project (Create React App or Vite)
- [ ] Install required dependencies:
  - [ ] axios or fetch for API calls
  - [ ] React Router (if multi-page)
  - [ ] UI library (Material-UI, Bootstrap, or custom CSS)
- [ ] Set up project structure (components, services, utils)

### 2. API Service Layer
- [ ] Create API service file for backend communication
- [ ] Implement `getCurrencies()` - GET `/api/v1/currencies`
- [ ] Implement `addCurrency(code)` - POST `/api/v1/currencies?currency={code}`
- [ ] Implement `convertCurrency(amount, from, to)` - GET `/api/v1/exchange`
- [ ] Handle authentication headers (if required)
- [ ] Handle API errors and display to user

### 3. Components

#### Currency List Component
- [ ] Display list of tracked currencies
- [ ] Fetch currencies on component mount
- [ ] Handle loading state
- [ ] Handle empty state (no currencies)
- [ ] Handle error state

#### Add Currency Component
- [ ] Create form with input field for currency code
- [ ] Add submit button
- [ ] Validate input (not empty)
- [ ] Call API to add currency
- [ ] Show success/error message
- [ ] Refresh currency list after successful addition
- [ ] Handle ADMIN authentication (login or token)

#### Currency Converter Component
- [ ] Create form with:
  - [ ] Amount input field
  - [ ] Source currency dropdown/select
  - [ ] Target currency dropdown/select
  - [ ] Convert button
- [ ] Populate currency dropdowns from tracked currencies
- [ ] Validate amount (positive number)
- [ ] Call API to convert currency
- [ ] Display converted amount
- [ ] Handle errors (invalid input, unsupported currency)

#### Exchange Rates Display Component
- [ ] Display all tracked currencies
- [ ] Display exchange rates between currency pairs
- [ ] Format numbers appropriately
- [ ] Auto-refresh or manual refresh option

### 4. State Management
- [ ] Manage currency list state
- [ ] Manage conversion result state
- [ ] Manage loading states
- [ ] Manage error states
- [ ] Consider using Context API or state management library if needed

### 5. Authentication (if ADMIN features needed)
- [ ] Create login form
- [ ] Store authentication token/session
- [ ] Show/hide ADMIN features based on role
- [ ] Handle logout

### 6. UI/UX
- [ ] Design responsive layout
- [ ] Add loading spinners
- [ ] Add error notifications/toasts
- [ ] Add success notifications
- [ ] Improve form validation feedback
- [ ] Add styling and polish

### 7. Testing (Frontend)
- [ ] Unit tests for API service functions
- [ ] Component tests for Currency List
- [ ] Component tests for Add Currency form
- [ ] Component tests for Currency Converter
- [ ] Integration tests for full user flows
- [ ] Test error handling scenarios

---

## 🔗 Integration & Deployment

### 1. Integration
- [ ] Configure CORS in Spring Boot to allow React frontend
- [ ] Test end-to-end flow from React UI to backend API
- [ ] Ensure authentication works across frontend and backend
- [ ] Verify all acceptance criteria are met

### 2. Documentation
- [ ] Create API documentation (Swagger/OpenAPI)
- [ ] Document authentication setup
- [ ] Add README with setup instructions
- [ ] Document environment variables and configuration
- [ ] Add architecture diagram (optional)

### 3. Deployment Preparation
- [ ] Create production build of React app
- [ ] Configure Spring Boot for production
- [ ] Set up environment-specific configurations
- [ ] Consider containerization (Docker)
- [ ] Prepare deployment scripts

### 4. Final Validation
- [ ] ✅ Verify all acceptance criteria scenarios pass
- [ ] ✅ Test with empty initial state
- [ ] ✅ Test ADMIN vs non-ADMIN access
- [ ] ✅ Test input validation (empty, negative values)
- [ ] ✅ Test currency conversion accuracy
- [ ] ✅ Test unsupported currency handling
- [ ] ✅ Test UI displays correctly
- [ ] ✅ Test UI can add currencies
- [ ] ✅ Verify in-memory storage persists during session
- [ ] ✅ Verify no external API calls are made

---

## 📊 Acceptance Criteria Mapping

### ✅ Currency Management
- [ ] ✓ Retrieve list of tracked currencies (GET)
- [ ] ✓ Add currency as ADMIN (POST with auth)
- [ ] ✓ Block add currency as non-ADMIN (403)
- [ ] ✓ Validate currency input (400 on empty)

### ✅ Currency Conversion
- [ ] ✓ Convert between tracked currencies
- [ ] ✓ Reject negative amounts (400)
- [ ] ✓ Handle unsupported currencies (404)

### ✅ System Behavior
- [ ] ✓ Initial empty currency list
- [ ] ✓ In-memory exchange rate storage
- [ ] ✓ No external API calls
- [ ] ✓ Clear error messages

### ✅ UI Interaction
- [ ] ✓ Display currencies and rates in React UI
- [ ] ✓ Add new currency through React UI (ADMIN)

---

## 🎯 Priority Order

### Phase 1: Core Backend (High Priority)
1. Project setup and dependencies
2. Data models and in-memory storage
3. Currency management API (GET & POST)
4. Basic security configuration
5. Exchange rate generation and storage

### Phase 2: Conversion & Error Handling (High Priority)
1. Currency conversion API
2. Input validation
3. Global error handling
4. Backend testing

### Phase 3: Frontend Core (Medium Priority)
1. React project setup
2. API service layer
3. Currency list display
4. Currency converter component

### Phase 4: Admin Features & Polish (Medium Priority)
1. Authentication implementation
2. Add currency UI (ADMIN only)
3. Exchange rates display
4. Enhanced UI/UX

### Phase 5: Testing & Deployment (Low Priority)
1. Comprehensive testing
2. Integration validation
3. Documentation
4. Deployment preparation

---

## 📝 Notes
- All exchange rates are **mock/random** - no external API integration required
- In-memory storage means data is lost on service restart
- ADMIN authentication can be simple (in-memory users) for MVP
- Focus on acceptance criteria compliance first, then enhance

---

**Last Updated:** October 16, 2025
