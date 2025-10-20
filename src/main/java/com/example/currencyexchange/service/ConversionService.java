package com.example.currencyexchange.service;

import com.example.currencyexchange.dto.ConversionResponse;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.InvalidAmountException;
import org.springframework.stereotype.Service;

@Service
public class ConversionService {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    public ConversionService(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Convert an amount from one currency to another
     */
    public ConversionResponse convertCurrency(double amount, String fromCurrency, String toCurrency) {
        // Validate amount
        if (amount < 0) {
            throw new InvalidAmountException("Amount cannot be negative");
        }

        // Validate currencies exist
        String fromCode = fromCurrency.toUpperCase();
        String toCode = toCurrency.toUpperCase();

        if (!currencyService.isCurrencyTracked(fromCode)) {
            throw new CurrencyNotFoundException("Currency not supported: " + fromCode);
        }

        if (!currencyService.isCurrencyTracked(toCode)) {
            throw new CurrencyNotFoundException("Currency not supported: " + toCode);
        }

        // Get exchange rate and calculate conversion
        double rate = exchangeRateService.getExchangeRate(fromCode, toCode);
        double convertedAmount = amount * rate;

        return new ConversionResponse(amount, fromCode, toCode, convertedAmount, rate);
    }
}
