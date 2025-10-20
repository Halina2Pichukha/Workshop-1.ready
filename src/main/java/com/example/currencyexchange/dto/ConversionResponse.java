package com.example.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResponse {
    private double amount;
    private String fromCurrency;
    private String toCurrency;
    private double convertedAmount;
    private double rate;
}
