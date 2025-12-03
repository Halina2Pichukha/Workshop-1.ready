import com.example.currencyexchange.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

package com.example.currencyexchange.controller;



@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCurrency_withAdminRole_shouldReturnNoContent() throws Exception {
        String currencyCode = "USD";

        mockMvc.perform(delete("/api/v1/currencies/{code}", currencyCode))
                .andExpect(status().isNoContent());

        verify(currencyService, times(1)).deleteCurrency(currencyCode);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCurrency_withUserRole_shouldReturnForbidden() throws Exception {
        String currencyCode = "USD";

        mockMvc.perform(delete("/api/v1/currencies/{code}", currencyCode))
                .andExpect(status().isForbidden());

        verify(currencyService, never()).deleteCurrency(anyString());
    }

    @Test
    void deleteCurrency_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        String currencyCode = "USD";

        mockMvc.perform(delete("/api/v1/currencies/{code}", currencyCode))
                .andExpect(status().isUnauthorized());

        verify(currencyService, never()).deleteCurrency(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCurrency_withEmptyCode_shouldCallServiceWithEmptyString() throws Exception {
        mockMvc.perform(delete("/api/v1/currencies/{code}", ""))
                .andExpect(status().isNotFound());

        verify(currencyService, never()).deleteCurrency(anyString());
    }
}