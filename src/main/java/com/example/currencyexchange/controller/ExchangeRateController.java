package com.example.currencyexchange.controller;

import com.example.currencyexchange.dto.ExchangeRateResponse;
import com.example.currencyexchange.dto.ExchangeRatesListResponse;
import com.example.currencyexchange.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * GET /api/v1/rates - Retrieve all exchange rates
     */
    @GetMapping
    public ResponseEntity<ExchangeRatesListResponse> getAllExchangeRates() {
        List<ExchangeRateResponse> rates = exchangeRateService.getAllRates()
                .values()
                .stream()
                .map(rate -> new ExchangeRateResponse(
                        rate.getFromCurrency(),
                        rate.getToCurrency(),
                        rate.getRate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ExchangeRatesListResponse(rates));
    }
}
