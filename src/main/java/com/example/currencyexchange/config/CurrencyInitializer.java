package com.example.currencyexchange.config;

import com.example.currencyexchange.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Initializes default currencies on application startup
 */
@Component
public class CurrencyInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyInitializer.class);
    
    private final CurrencyService currencyService;

    // Default currencies to initialize
    private static final List<String> DEFAULT_CURRENCIES = Arrays.asList(
        "USD", // US Dollar
        "EUR", // Euro
        "GBP", // British Pound
        "JPY", // Japanese Yen
        "CHF", // Swiss Franc
        "CAD", // Canadian Dollar
        "AUD"  // Australian Dollar
    );

    public CurrencyInitializer(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void run(String... args) {
        logger.info("Initializing default currencies...");
        
        for (String currency : DEFAULT_CURRENCIES) {
            try {
                currencyService.addCurrency(currency);
                logger.info("Added default currency: {}", currency);
            } catch (Exception e) {
                logger.error("Failed to add currency {}: {}", currency, e.getMessage());
            }
        }
        
        logger.info("Default currencies initialized successfully. Total currencies: {}", 
                    currencyService.getAllCurrencies().size());
    }
}
