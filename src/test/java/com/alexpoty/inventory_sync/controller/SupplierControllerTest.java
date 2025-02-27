package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.exception.supplier.SupplierNotFoundException;
import com.alexpoty.inventory_sync.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SupplierController.class)
@AutoConfigureMockMvc(addFilters = false)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockitoBean
    private SupplierService service;

    @Test
    public void should_create_supplier() throws Exception {
        // when
        when(service.createSupplier(any(SupplierRequest.class))).thenReturn(createSupplierResponse());
        ResultActions resultActions = mockMvc.perform(post("/supplier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createSupplierRequest())));
        // assert
        resultActions.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void should_update_supplier() throws Exception {
        // when
        when(service.updateSupplier(anyLong(), any(SupplierRequest.class))).thenReturn(createSupplierResponse());
        ResultActions resultActions = mockMvc.perform(put("/supplier/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createSupplierRequest())));
        // assert
        resultActions.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void should_return_status_404_when_updating_unknown_supplier() throws Exception {
        // when
        doThrow(SupplierNotFoundException.class).when(service).updateSupplier(anyLong(), any(SupplierRequest.class));
        ResultActions resultActions = mockMvc.perform(put("/supplier/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createSupplierRequest())));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void should_get_a_list_of_suppliers() throws Exception {
        // when
        when(service.getAllSuppliers()).thenReturn(List.of(createSupplierResponse()));
        ResultActions resultActions = mockMvc.perform(get("/supplier")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void should_get_a_supplier_by_id() throws Exception {
        // when
        when(service.getSupplier(anyLong())).thenReturn(createSupplierResponse());
        ResultActions resultActions = mockMvc.perform(get("/supplier/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void should_return_status_404_when_getting_unknown_supplier() throws Exception {
        // when
        doThrow(SupplierNotFoundException.class).when(service).getSupplier(anyLong());
        ResultActions resultActions = mockMvc.perform(get("/supplier/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void should_delete_a_supplier() throws Exception {
        // when
        doNothing().when(service).deleteSupplier(anyLong());
        ResultActions resultActions = mockMvc.perform(delete("/supplier/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void should_return_status_404_when_deleting_unknown_supplier() throws Exception {
        // when
        doThrow(SupplierNotFoundException.class).when(service).deleteSupplier(anyLong());
        ResultActions resultActions = mockMvc.perform(delete("/supplier/1")
                .contentType(MediaType.APPLICATION_JSON));
        // assert
        resultActions.andExpect(status().isNotFound());
    }

    private SupplierRequest createSupplierRequest() {
        return new SupplierRequest("Test", "Test");
    }

    private SupplierResponse createSupplierResponse() {
        return new SupplierResponse(1L, "Test", "Test");
    }
}