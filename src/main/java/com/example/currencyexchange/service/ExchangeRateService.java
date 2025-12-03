package com.example.currencyexchange.service;

import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.model.ExchangeRate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExchangeRateService {

    private final ConcurrentHashMap<String, ExchangeRate> exchangeRates = new ConcurrentHashMap<>();

    /**
     * Generate a random exchange rate between 0.5 and 2.0
     */
    private double generateRandomRate() {
        return ThreadLocalRandom.current().nextDouble(0.5, 2.0);
    }

    /**
     * Generate bidirectional exchange rates for a currency pair
     */
    public void generateRateForCurrencyPair(String fromCurrency, String toCurrency) {
        String key1 = createKey(fromCurrency, toCurrency);
        String key2 = createKey(toCurrency, fromCurrency);

        // Check if rate already exists
        if (exchangeRates.containsKey(key1)) {
            return;
        }

        // Generate rate from currency1 to currency2
        double rate = generateRandomRate();
        ExchangeRate rate1 = new ExchangeRate(fromCurrency, toCurrency, rate);
        exchangeRates.put(key1, rate1);

        // Generate inverse rate (ensure consistency)
        double inverseRate = 1.0 / rate;
        ExchangeRate rate2 = new ExchangeRate(toCurrency, fromCurrency, inverseRate);
        exchangeRates.put(key2, rate2);
    }

    /**
     * Get exchange rate for a currency pair
     */
    public double getExchangeRate(String fromCurrency, String toCurrency) {
        // Same currency conversion
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return 1.0;
        }

        String key = createKey(fromCurrency.toUpperCase(), toCurrency.toUpperCase());
        
        if (!exchangeRates.containsKey(key)) {
            throw new CurrencyNotFoundException(
                String.format("Exchange rate not found for %s to %s", fromCurrency, toCurrency)
            );
        }

        return exchangeRates.get(key).getRate();
    }

    /**
     * Create a unique key for currency pair
     */
    private String createKey(String fromCurrency, String toCurrency) {
        return fromCurrency.toUpperCase() + "_" + toCurrency.toUpperCase();
    }

    /**
     * Get all exchange rates (for testing/debugging)
     */
    public ConcurrentHashMap<String, ExchangeRate> getAllRates() {
        return new ConcurrentHashMap<>(exchangeRates);
    }

    /**
     * Remove all exchange rates involving a specific currency
     */
    public void removeRatesForCurrency(String currencyCode) {
        String code = currencyCode.toUpperCase();
        exchangeRates.keySet().removeIf(key -> key.startsWith(code + "_") || key.endsWith("_" + code));
    }
}
