package com.example.currencyexchange.controller;

import com.example.currencyexchange.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExchangeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @Test
    void testConvertCurrency() throws Exception {
        mockMvc.perform(get("/api/v1/exchange")
                        .param("amount", "100")
                        .param("from", "USD")
                        .param("to", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("EUR"))
                .andExpect(jsonPath("$.convertedAmount").exists())
                .andExpect(jsonPath("$.rate").exists());
    }

    @Test
    void testConvertWithNegativeAmountShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/exchange")
                        .param("amount", "-100")
                        .param("from", "USD")
                        .param("to", "EUR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount cannot be negative"));
    }

    @Test
    void testConvertWithUnsupportedFromCurrencyShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/exchange")
                        .param("amount", "100")
                        .param("from", "XXX")
                        .param("to", "EUR"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Currency not supported: XXX"));
    }

    @Test
    void testConvertWithUnsupportedToCurrencyShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/exchange")
                        .param("amount", "100")
                        .param("from", "USD")
                        .param("to", "YYY"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Currency not supported: YYY"));
    }

    @Test
    void testConvertZeroAmount() throws Exception {
        mockMvc.perform(get("/api/v1/exchange")
                        .param("amount", "0")
                        .param("from", "USD")
                        .param("to", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.convertedAmount").value(0));
    }
}
