package com.example.currencyexchange.controller;

import com.example.currencyexchange.dto.ConversionResponse;
import com.example.currencyexchange.service.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
public class ExchangeController {

    private final ConversionService conversionService;

    public ExchangeController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * GET /api/v1/exchange?amount={amount}&from={from}&to={to} - Convert currency
     */
    @GetMapping
    public ResponseEntity<ConversionResponse> convertCurrency(
            @RequestParam("amount") double amount,
            @RequestParam("from") String from,
            @RequestParam("to") String to) {
        
        ConversionResponse response = conversionService.convertCurrency(amount, from, to);
        return ResponseEntity.ok(response);
    }
}
