package com.example.currencyexchange.controller;

import com.example.currencyexchange.dto.CurrencyListResponse;
import com.example.currencyexchange.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * GET /api/v1/currencies - Retrieve all tracked currencies
     */
    @GetMapping
    public ResponseEntity<CurrencyListResponse> getCurrencies() {
        List<String> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(new CurrencyListResponse(currencies));
    }

    /**
     * POST /api/v1/currencies?currency={code} - Add a new currency (ADMIN only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addCurrency(@RequestParam("currency") String currency) {
        currencyService.addCurrency(currency);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * DELETE /api/v1/currencies/{code} - Delete a currency (ADMIN only)
     */

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCurrency(@PathVariable("code") String code) {
        currencyService.deleteCurrency(code);
        return ResponseEntity.noContent().build();
    }
}
