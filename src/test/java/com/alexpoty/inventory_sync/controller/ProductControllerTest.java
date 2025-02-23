package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectMapper mapper;

    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productResponse = new ProductResponse(
                1L,
                "Test",
                "Test",
                new BigDecimal(123),
                1,
                1L,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void should_save_product_and_return_status_201() throws Exception {
        // when
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productResponse)));
        // assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test")));
    }
}