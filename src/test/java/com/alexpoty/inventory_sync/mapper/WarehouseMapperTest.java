package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.model.Warehouse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseMapperTest {

    private final WarehouseMapper warehouseMapper = new WarehouseMapperImpl();

    @Test
    public void should_convert_to_warehouse() {
        // when
        Warehouse actual = warehouseMapper.toWarehouse(createProductRequest());
        // assert
        assertNotNull(actual);
        assertEquals(actual.getName(), "Test");
    }

    @Test
    public void should_convert_warehouse_to_response() {
        // when
        WarehouseResponse actual = warehouseMapper.toWarehouseResponse(createWarehouse());
        // assert
        assertNotNull(actual);
        assertEquals(actual.id(), 1L);
        assertEquals(actual.name(), "Test");
    }

    private WarehouseRequest createProductRequest() {
        return new WarehouseRequest(
                "Test",
                "Test"
        );
    }

    private Warehouse createWarehouse() {
        return Warehouse.builder()
                .id(1L)
                .name("Test")
                .location("Test")
                .build();
    }
}