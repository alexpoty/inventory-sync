package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.exception.warehouse.WarehouseNotFoundException;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import com.alexpoty.inventory_sync.service.impl.WarehouseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Test
    public void should_create_warehouse() {
        // given
        WarehouseRequest warehouseRequest = createRequest();
        // when
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(createWarehouse());
        WarehouseResponse actual = warehouseService.addWarehouse(warehouseRequest);
        // assert
        assertNotNull(actual);
        assertEquals(warehouseRequest.name(), actual.name());
        assertEquals(warehouseRequest.location(), actual.location());
        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
    }

    @Test
    public void should_get_all_warehouses() {
        //when
        when(warehouseRepository.findAll()).thenReturn(List.of(createWarehouse()));
        List<WarehouseResponse> responses = warehouseService.getWarehouses();
        // assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(warehouseRepository, times(1)).findAll();
    }

    @Test
    public void should_get_warehouse_by_id() {
        // when
        when(warehouseRepository.findById(any(Long.class))).thenReturn(Optional.of(createWarehouse()));
        WarehouseResponse response = warehouseService.getWarehouse(1L);
        // assert
        assertNotNull(response);
        assertEquals("Test", response.name());
        assertEquals("Test", response.location());
        verify(warehouseRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void should_throw_when_finding_by_id() {
        // when
        when(warehouseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // assert
        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.getWarehouse(1L));
    }

    @Test
    public void should_update_warehouse() {
        // given
        WarehouseRequest warehouseRequest = createRequest();
        // when
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(createWarehouse());
        when(warehouseRepository.findById(any(Long.class))).thenReturn(Optional.of(createWarehouse()));
        WarehouseResponse response = warehouseService.updateWarehouse(1L, warehouseRequest);
        // assert
        assertNotNull(response);
        assertEquals(warehouseRequest.name(), response.name());
        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
    }

    @Test
    public void should_throw_when_updating() {
        WarehouseRequest warehouseRequest = createRequest();
        //when
        when(warehouseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // assert
        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.updateWarehouse(1L, warehouseRequest));
    }

    @Test
    public void should_delete_warehouse() {
        // when
        when(warehouseRepository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(warehouseRepository).deleteById(any(Long.class));
        warehouseService.deleteWarehouse(1L);
        // assert
        verify(warehouseRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void should_throw_when_deleting() {
        // when
        when(warehouseRepository.existsById(any(Long.class))).thenReturn(false);
        // assert
        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.deleteWarehouse(1L));
    }

    private Warehouse createWarehouse() {
        return Warehouse.builder()
                .id(1L)
                .name("Test")
                .location("Test")
                .build();
    }

    private WarehouseRequest createRequest() {
        return new WarehouseRequest(
                "Test",
                "Test"
        );
    }
}