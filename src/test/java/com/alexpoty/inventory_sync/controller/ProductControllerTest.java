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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;
    @Autowired
    private ObjectMapper mapper;

    private ProductResponse productResponse;
    private ProductRequest productRequest;

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
        productRequest = new ProductRequest("Test",
                "Test",
                new BigDecimal(123),
                1,
                1L);
    }

    @Test
    void should_save_product_and_return_status_201() throws Exception {
        // when
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)));
        // assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    void should_return_bad_request_when_save_product_name_is_not_valid() throws Exception {
        // given
        ProductRequest badRequest = new ProductRequest("",
                "Test",
                new BigDecimal(123),
                1,
                1L);
        // when
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badRequest)));
        // assert
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void should_get_product_by_id_status_200() throws Exception {
        // when
        when(productService.getProduct(any(Long.class))).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    void should_get_product_page_status_200() throws Exception {
        // when
        when(productService.getProducts(any(Integer.class), any(Integer.class))).thenReturn(new PageImpl<>(List.of(productResponse)));
        ResultActions resultActions = mockMvc.perform(get("/products?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk());
    }

    @Test
    void should_get_products_by_warehouseId_status_200() throws Exception {
        // when
        when(productService.getProductsByWarehouseId(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new PageImpl<>(List.of(productResponse)));
        ResultActions resultActions = mockMvc.perform(get("/products?warehouseId=1&page=2&size=2")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk());
    }
}