# Currency Exchange Rates Provider Service - Acceptance Criteria

## Feature: Currency Management

### Scenario: Retrieve the list of tracked currencies
Given the service is running  
When a user sends a GET request to `/api/v1/currencies`  
Then the response status should be 200 OK  
And the response body should contain a list of currently tracked currencies  

### Scenario: Add a new currency as ADMIN
Given a user has ADMIN privileges  
When the user sends a POST request to `/api/v1/currencies?currency=USD`  
Then the response status should be 201 Created  
And the currency list should include "USD"  
And a mock exchange rate should be generated for USD against all other tracked currencies  

### Scenario: Add a new currency as non-ADMIN
Given a user does not have ADMIN privileges  
When the user sends a POST request to `/api/v1/currencies?currency=USD`  
Then the response status should be 403 Forbidden  
And the currency list should not include "USD"  

### Scenario: Validate currency input
Given a user has ADMIN privileges  
When the user sends a POST request to `/api/v1/currencies?currency=`  
Then the response status should be 400 Bad Request  
And the response body should indicate that the currency value cannot be empty  

## Feature: Currency Conversion

### Scenario: Convert amount between two tracked currencies
Given "USD" and "EUR" are tracked currencies  
And the service has a mock exchange rate for USD to EUR  
When a user sends a GET request to `/api/v1/exchange?amount=15&from=USD&to=EUR`  
Then the response status should be 200 OK  
And the response body should contain the converted amount as a positive number  

### Scenario: Convert amount with invalid parameters
When a user sends a GET request to `/api/v1/exchange?amount=-10&from=USD&to=EUR`  
Then the response status should be 400 Bad Request  
And the response body should indicate that the amount must be positive  

### Scenario: Convert amount for unsupported currency
When a user sends a GET request to `/api/v1/exchange?amount=15&from=USD&to=GBP`  
Then the response status should be 404 Not Found  
And the response body should indicate that the target currency is not tracked  

## Feature: System Behavior

### Scenario: Initial currency list is empty
Given the service is freshly started  
When a user sends a GET request to `/api/v1/currencies`  
Then the response status should be 200 OK  
And the response body should be an empty list  

### Scenario: Exchange rates are stored in memory
Given currencies are tracked  
When a user performs multiple conversions  
Then the same in-memory exchange rates should be used  
And no external API calls should be made  

### Scenario: Error handling
When a user sends an invalid request  
Then the service should return a clear error message  
And the response status should indicate the type of error  

## Feature: UI Interaction

### Scenario: Display exchange rates in React UI
Given the service has tracked currencies  
When a user opens the React UI  
Then the list of tracked currencies and their rates should be displayed  

### Scenario: Add new currency through React UI
Given a user has ADMIN privileges  
When the user enters a new currency in the UI and submits  
Then the currency should be added to the service  
And the updated list should be displayed in the UI

