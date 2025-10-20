const API_BASE_URL = 'http://localhost:8080/api/v1';

// API Service for Currency Exchange
const api = {
  // Get all tracked currencies
  getCurrencies: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/currencies`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching currencies:', error);
      throw error;
    }
  },

  // Add a new currency (requires ADMIN authentication)
  addCurrency: async (currencyCode, username, password) => {
    try {
      const credentials = btoa(`${username}:${password}`);
      const response = await fetch(`${API_BASE_URL}/currencies?currency=${currencyCode}`, {
        method: 'POST',
        headers: {
          'Authorization': `Basic ${credentials}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.status === 403) {
        throw new Error('Access denied. ADMIN role required.');
      }
      if (response.status === 400) {
        const error = await response.json();
        throw new Error(error.message || 'Invalid currency code');
      }
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      return response.status === 201;
    } catch (error) {
      console.error('Error adding currency:', error);
      throw error;
    }
  },

  // Convert currency
  convertCurrency: async (amount, fromCurrency, toCurrency) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/exchange?amount=${amount}&from=${fromCurrency}&to=${toCurrency}`
      );

      if (response.status === 400) {
        const error = await response.json();
        throw new Error(error.message || 'Invalid request');
      }
      if (response.status === 404) {
        const error = await response.json();
        throw new Error(error.message || 'Currency not found');
      }
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error converting currency:', error);
      throw error;
    }
  },

  // Get all exchange rates
  getExchangeRates: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/rates`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error fetching exchange rates:', error);
      throw error;
    }
  },
};

export default api;
