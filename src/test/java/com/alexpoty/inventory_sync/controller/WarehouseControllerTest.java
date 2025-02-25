package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.exception.warehouse.WarehouseNotFoundException;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.service.WarehouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private WarehouseService warehouseService;

    @Test
    public void should_get_warehouses() throws Exception {
        // when
        when(warehouseService.getWarehouses()).thenReturn(List.of(createResponse()));
        ResultActions resultActions = mockMvc.perform(get("/warehouses")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void should_get_warehouse_by_id() throws Exception {
        // when
        when(warehouseService.getWarehouse(any(Long.class))).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(get("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    public void should_throw_when_warehouse_not_found() throws Exception {
        // when
        when(warehouseService.getWarehouse(any(Long.class))).thenThrow(WarehouseNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(get("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void should_create_warehouse() throws Exception {
        // given
        WarehouseRequest warehouseRequest = createRequest();
        // when
        when(warehouseService.addWarehouse(any(WarehouseRequest.class))).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseRequest)));
        // assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    public void should_throw_when_invalid_request() throws Exception {
        // given
        WarehouseRequest badRequest = new WarehouseRequest(
                "",
                "Test"
        );
        // when
        when(warehouseService.addWarehouse(any(WarehouseRequest.class))).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)));
        // assert
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_warehouse() throws Exception {
        // given
        WarehouseRequest warehouseRequest = createRequest();
        // when
        when(warehouseService.updateWarehouse(any(Long.class), any(WarehouseRequest.class))).thenReturn(createResponse());
        ResultActions resultActions = mockMvc.perform(post("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseRequest)));
        // assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void should_throw_when_updating_not_existing_warehouse() throws Exception {
        //given
        WarehouseRequest warehouseRequest = createRequest();
        //when
        when(warehouseService.updateWarehouse(any(Long.class), any(WarehouseRequest.class)))
                .thenThrow(WarehouseNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(post("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouseRequest)));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void should_delete_warehouse() throws Exception {
        // when
        doNothing().when(warehouseService).deleteWarehouse(any(Long.class));
        ResultActions resultActions = mockMvc.perform(delete("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void should_throw_when_deleting_not_existing_warehouse() throws Exception {
        // when
        doThrow(WarehouseNotFoundException.class).when(warehouseService).deleteWarehouse(any(Long.class));
        ResultActions resultActions = mockMvc.perform(delete("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    private WarehouseRequest createRequest() {
        return new WarehouseRequest(
                "Test",
                "Test"
        );
    }

    private WarehouseResponse createResponse() {
        return new WarehouseResponse(
                1L,
                "Test",
                "Test",
                List.of(Product.builder()
                        .name("Test")
                        .description("Test")
                        .price(new BigDecimal(123))
                        .quantity(1)
                        .created_at(Instant.now())
                        .updated_at(Instant.now())
                        .build())
        );
    }

}