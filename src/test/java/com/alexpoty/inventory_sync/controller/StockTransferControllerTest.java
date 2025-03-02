package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.service.StockTransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockTransferController.class)
@AutoConfigureMockMvc
class StockTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockitoBean
    private StockTransferService stockTransferService;

    @Test
    public void should_get_products_by_warehouse_id() throws Exception {
        // when
        when(stockTransferService.getProductsByWarehouseId(any(Long.class)))
                .thenReturn(List.of(createResponse()));
        ResultActions resultActions = mockMvc.perform(get("/transfer/warehouse/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(1)));
    }

    @Test
    public void should_get_warehouses_by_product_id() throws Exception {
        // when
        when(stockTransferService.getWarehousesByProductId(any(Long.class)))
                .thenReturn(List.of(createResponse()));
        ResultActions resultActions = mockMvc.perform(get("/transfer/products/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(1)));
    }

    @Test
    public void should_add_stock_transfer() throws Exception {
        // when
        when(stockTransferService.addStock(any(StockTransferRequest.class))).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createRequest())));
        // assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.is(1)));
    }

    @Test
    public void transfer_ShouldTransferSuccessfully() throws Exception {
        // when
        when(stockTransferService.transferStock(any(StockTransferRequest.class))).thenReturn(List.of(createResponse()));
        ResultActions resultActions = mockMvc.perform(put("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createRequest())));
        // assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(1)));
    }

    @Test
    public void getStockById_ShouldReturnStock() throws Exception {
        // when
        when(stockTransferService.getStock(anyLong())).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(get("/transfer/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(jsonPath("$.quantity", CoreMatchers.is(100)));
    }

    @Test
    public void getStockById_ShouldReturnStatusNotFound() throws Exception {
        // when
        when(stockTransferService.getStock(anyLong())).thenThrow(ProductNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(get("/transfer/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void addQuantity_ShouldAddQuantityAndReturnResponse() throws Exception {
        // when
        when(stockTransferService.addQuantity(any(Integer.class), any(Long.class))).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(put("/transfer/quantity?quantity=100&id=1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.quantity", CoreMatchers.is(100)));
    }

    @Test
    public void addQuantity_ShouldReturnStatusNotFound() throws Exception {
        // when
        when(stockTransferService.addQuantity(any(Integer.class), anyLong())).thenThrow(ProductNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(put("/transfer/quantity?quantity=100&id=1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    private StockTransferResponse createResponse() {
        return new StockTransferResponse(
                1L,
                Product.builder()
                        .id(1L)
                        .name("Test")
                        .description("Test")
                        .price(new BigDecimal(123))
                        .created_at(Instant.now())
                        .updated_at(Instant.now())
                        .build(),
                Warehouse.builder()
                        .id(1L)
                        .name("Test")
                        .location("Test")
                        .build(),
                100
        );
    }
    private StockTransferRequest createRequest() {
        return new StockTransferRequest(
                1L,
                1L,
                2L,
                200
        );
    }
}