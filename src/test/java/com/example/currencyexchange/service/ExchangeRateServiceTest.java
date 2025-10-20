package com.example.currencyexchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService();
    }

    @Test
    void testGenerateRateForCurrencyPair() {
        exchangeRateService.generateRateForCurrencyPair("USD", "EUR");
        
        double usdToEur = exchangeRateService.getExchangeRate("USD", "EUR");
        double eurToUsd = exchangeRateService.getExchangeRate("EUR", "USD");
        
        assertTrue(usdToEur >= 0.5 && usdToEur <= 2.0, "Rate should be between 0.5 and 2.0");
        assertTrue(eurToUsd >= 0.5 && eurToUsd <= 2.0, "Inverse rate should be between 0.5 and 2.0");
    }

    @Test
    void testBidirectionalRateConsistency() {
        exchangeRateService.generateRateForCurrencyPair("USD", "EUR");
        
        double usdToEur = exchangeRateService.getExchangeRate("USD", "EUR");
        double eurToUsd = exchangeRateService.getExchangeRate("EUR", "USD");
        
        double product = usdToEur * eurToUsd;
        assertEquals(1.0, product, 0.0001, "Forward and inverse rates should multiply to 1.0");
    }

    @Test
    void testSameCurrencyConversion() {
        double rate = exchangeRateService.getExchangeRate("USD", "USD");
        assertEquals(1.0, rate, "Same currency conversion should return 1.0");
    }

    @Test
    void testMultipleCurrencyPairs() {
        exchangeRateService.generateRateForCurrencyPair("USD", "EUR");
        exchangeRateService.generateRateForCurrencyPair("USD", "GBP");
        exchangeRateService.generateRateForCurrencyPair("EUR", "GBP");
        
        assertDoesNotThrow(() -> {
            exchangeRateService.getExchangeRate("USD", "EUR");
            exchangeRateService.getExchangeRate("USD", "GBP");
            exchangeRateService.getExchangeRate("EUR", "GBP");
            exchangeRateService.getExchangeRate("GBP", "EUR");
        });
    }
}
