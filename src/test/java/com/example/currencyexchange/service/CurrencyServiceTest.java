package com.example.currencyexchange.service;

import com.example.currencyexchange.exception.InvalidCurrencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {

    private CurrencyService currencyService;
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService(exchangeRateService);
    }

    @Test
    void testInitialStateShouldBeEmpty() {
        List<String> currencies = currencyService.getAllCurrencies();
        assertTrue(currencies.isEmpty(), "Initial currency list should be empty");
    }

    @Test
    void testAddCurrency() {
        currencyService.addCurrency("USD");
        
        List<String> currencies = currencyService.getAllCurrencies();
        assertEquals(1, currencies.size());
        assertTrue(currencies.contains("USD"));
    }

    @Test
    void testAddMultipleCurrencies() {
        currencyService.addCurrency("USD");
        currencyService.addCurrency("EUR");
        currencyService.addCurrency("GBP");
        
        List<String> currencies = currencyService.getAllCurrencies();
        assertEquals(3, currencies.size());
        assertTrue(currencies.contains("USD"));
        assertTrue(currencies.contains("EUR"));
        assertTrue(currencies.contains("GBP"));
    }

    @Test
    void testAddEmptyCurrencyShouldThrowException() {
        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.addCurrency("");
        });
    }

    @Test
    void testAddNullCurrencyShouldThrowException() {
        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.addCurrency(null);
        });
    }

    @Test
    void testAddCurrencyWithWhitespaceShouldThrowException() {
        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.addCurrency("   ");
        });
    }

    @Test
    void testAddDuplicateCurrency() {
        currencyService.addCurrency("USD");
        currencyService.addCurrency("USD");
        
        List<String> currencies = currencyService.getAllCurrencies();
        assertEquals(1, currencies.size(), "Duplicate currency should not be added");
    }

    @Test
    void testIsCurrencyTracked() {
        currencyService.addCurrency("USD");
        
        assertTrue(currencyService.isCurrencyTracked("USD"));
        assertFalse(currencyService.isCurrencyTracked("EUR"));
    }

    @Test
    void testCurrencyCodeIsCaseInsensitive() {
        currencyService.addCurrency("usd");
        
        assertTrue(currencyService.isCurrencyTracked("USD"));
        assertTrue(currencyService.isCurrencyTracked("usd"));
        assertTrue(currencyService.isCurrencyTracked("Usd"));
    }
}
