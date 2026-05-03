package com.leonardo.orderprocessing.interfaces.rest;

import com.leonardo.orderprocessing.IntegrationTestBase;
import com.leonardo.orderprocessing.infrastructure.kafka.OrderEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderEventProducer eventProducer;

    @Test
    void shouldCreateOrderAndReturn201() throws Exception {
        // Assemble
        String body = """
                {"amount": 250.00}
                """;

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.amount").value(250.00));
    }

    @Test
    void shouldReturn400WhenAmountIsNull() throws Exception {
        // Assemble
        String body = """
                {"amount": null}
                """;

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAmountIsZero() throws Exception {
        // Assemble
        String body = """
                {"amount": 0}
                """;

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
