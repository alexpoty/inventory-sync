package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
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
                Instant.now(),
                Instant.now()
        );
        productRequest = new ProductRequest("Test",
                "Test",
                new BigDecimal(123)
        );
    }

    @Test
    public void should_save_product_and_return_status_201() throws Exception {
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
    public void should_return_bad_request_when_save_product_name_is_not_valid() throws Exception {
        // given
        ProductRequest badRequest = new ProductRequest("",
                "Test",
                new BigDecimal(123)
        );
        // when
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badRequest)));
        // assert
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_product_by_id_status_200() throws Exception {
        // when
        when(productService.getProduct(any(Long.class))).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    public void should_get_product_by_id_status_404() throws Exception {
        // when
        when(productService.getProduct(any(Long.class))).thenThrow(new ProductNotFoundException("Product not found"));
        ResultActions resultActions = mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound()).andExpect(jsonPath("$.error", is("Product not found")));
    }

    @Test
    public void should_get_product_page_status_200() throws Exception {
        // when
        when(productService.getProducts(any(Integer.class), any(Integer.class))).thenReturn(new PageImpl<>(List.of(productResponse)));
        ResultActions resultActions = mockMvc.perform(get("/products?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].name", is("Test")));
    }

    @Test
    public void should_update_product_status_created() throws Exception {
        // when
        when(productService.updateProduct(any(Long.class), any(ProductRequest.class))).thenReturn(productResponse);
        ResultActions resultActions = mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    public void should_throw_status_404_when_update_product_not_found() throws Exception {
        // when
        when(productService.updateProduct(any(Long.class), any(ProductRequest.class)))
                .thenThrow(new ProductNotFoundException("Product not found"));
        ResultActions resultActions = mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)));
        // assert
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Product not found")));
    }

    @Test
    public void should_delete_product_by_id_status_no_content() throws Exception {
        // when
        doNothing().when(productService).deleteProduct(any());
        ResultActions resultActions = mockMvc.perform(delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void should_throw_status_404_when_delete_product_not_found() throws Exception {
        // when
        doThrow(new ProductNotFoundException("Product not found")).when(productService).deleteProduct(any());
        ResultActions resultActions = mockMvc.perform(delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }
 }