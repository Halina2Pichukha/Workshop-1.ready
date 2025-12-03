package com.example.currencyexchange.service;

import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.InvalidCurrencyException;
import com.example.currencyexchange.model.Currency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyService {

    private final ConcurrentHashMap<String, Currency> currencies = new ConcurrentHashMap<>();
    private final ExchangeRateService exchangeRateService;

    public CurrencyService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Get all tracked currencies
     */
    public List<String> getAllCurrencies() {
        return new ArrayList<>(currencies.keySet());
    }

    /**
     * Add a new currency
     */
    public void addCurrency(String currencyCode) {
        // Validate input
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new InvalidCurrencyException("Currency code cannot be empty");
        }

        String code = currencyCode.trim().toUpperCase();
        
        // Check if currency already exists
        if (currencies.containsKey(code)) {
            return; // Already exists, no need to add again
        }

        // Add the currency
        Currency currency = new Currency(code);
        currencies.put(code, currency);

        // Generate exchange rates for the new currency against all existing currencies
        List<String> existingCurrencies = new ArrayList<>(currencies.keySet());
        for (String existingCode : existingCurrencies) {
            if (!existingCode.equals(code)) {
                exchangeRateService.generateRateForCurrencyPair(code, existingCode);
            }
        }
    }

    /**
     * Check if a currency is tracked
     */
    public boolean isCurrencyTracked(String currencyCode) {
        return currencies.containsKey(currencyCode.toUpperCase());
    }

    /**
     * Get a currency by code
     */
    public Currency getCurrency(String currencyCode) {
        String code = currencyCode.toUpperCase();
        if (!currencies.containsKey(code)) {
            throw new CurrencyNotFoundException("Currency not found: " + code);
        }
        return currencies.get(code);
    }

    /**
     * Delete a currency
     */
    public void deleteCurrency(String currencyCode) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new InvalidCurrencyException("Currency code cannot be empty");
        }

        String code = currencyCode.trim().toUpperCase();
        
        if (!currencies.containsKey(code)) {
            throw new CurrencyNotFoundException("Currency not found: " + code);
        }

        // Remove the currency
        currencies.remove(code);

        // Remove all exchange rates involving this currency
        exchangeRateService.removeRatesForCurrency(code);
    }
}
