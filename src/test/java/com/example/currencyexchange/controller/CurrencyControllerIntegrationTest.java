package com.example.currencyexchange.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCurrenciesInitiallyEmpty() throws Exception {
        // With default currencies initialized (USD, EUR, GBP, JPY, CHF, CAD, AUD)
        mockMvc.perform(get("/api/v1/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies.length()").value(7));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddCurrencyAsAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/currencies")
                        .param("currency", "USD"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddCurrencyAsUserShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/currencies")
                        .param("currency", "USD"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddCurrencyWithoutAuthShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/currencies")
                        .param("currency", "USD"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddEmptyCurrencyShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/currencies")
                        .param("currency", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Currency code cannot be empty"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCurrenciesAfterAddingCurrencies() throws Exception {
        // Add new currencies (not in default list: USD, EUR, GBP, JPY, CHF, CAD, AUD)
        mockMvc.perform(post("/api/v1/currencies")
                        .param("currency", "NZD"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/currencies")
                        .param("currency", "SGD"))
                .andExpect(status().isCreated());

        // Get currencies - should have 7 default + 2 new = 9
        mockMvc.perform(get("/api/v1/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies.length()").value(9));
    }
}
