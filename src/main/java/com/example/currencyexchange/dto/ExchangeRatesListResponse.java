package com.example.currencyexchange.dto;

import java.util.List;

public class ExchangeRatesListResponse {
    private List<ExchangeRateResponse> rates;

    public ExchangeRatesListResponse() {
    }

    public ExchangeRatesListResponse(List<ExchangeRateResponse> rates) {
        this.rates = rates;
    }

    public List<ExchangeRateResponse> getRates() {
        return rates;
    }

    public void setRates(List<ExchangeRateResponse> rates) {
        this.rates = rates;
    }
}
