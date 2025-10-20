package com.example.currencyexchange.service;

import com.example.currencyexchange.dto.ConversionResponse;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversionServiceTest {

    private ConversionService conversionService;
    private CurrencyService currencyService;
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService(exchangeRateService);
        conversionService = new ConversionService(currencyService, exchangeRateService);
        
        // Add test currencies
        currencyService.addCurrency("USD");
        currencyService.addCurrency("EUR");
    }

    @Test
    void testConvertCurrency() {
        ConversionResponse response = conversionService.convertCurrency(100, "USD", "EUR");
        
        assertNotNull(response);
        assertEquals(100, response.getAmount());
        assertEquals("USD", response.getFromCurrency());
        assertEquals("EUR", response.getToCurrency());
        assertTrue(response.getConvertedAmount() > 0);
        assertTrue(response.getRate() > 0);
    }

    @Test
    void testConvertWithNegativeAmountShouldThrowException() {
        assertThrows(InvalidAmountException.class, () -> {
            conversionService.convertCurrency(-100, "USD", "EUR");
        });
    }

    @Test
    void testConvertWithUnsupportedFromCurrencyShouldThrowException() {
        assertThrows(CurrencyNotFoundException.class, () -> {
            conversionService.convertCurrency(100, "GBP", "EUR");
        });
    }

    @Test
    void testConvertWithUnsupportedToCurrencyShouldThrowException() {
        assertThrows(CurrencyNotFoundException.class, () -> {
            conversionService.convertCurrency(100, "USD", "GBP");
        });
    }

    @Test
    void testConvertZeroAmount() {
        ConversionResponse response = conversionService.convertCurrency(0, "USD", "EUR");
        
        assertNotNull(response);
        assertEquals(0, response.getConvertedAmount());
    }

    @Test
    void testConversionCalculation() {
        ConversionResponse response = conversionService.convertCurrency(100, "USD", "EUR");
        
        double expectedAmount = 100 * response.getRate();
        assertEquals(expectedAmount, response.getConvertedAmount(), 0.0001);
    }
}
