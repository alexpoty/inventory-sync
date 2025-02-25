package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    List<WarehouseResponse> getWarehouses();
    WarehouseResponse getWarehouse(Long id);
    WarehouseResponse addWarehouse(WarehouseRequest warehouseRequest);
    WarehouseResponse updateWarehouse(Long id, WarehouseRequest warehouseRequest);
    void deleteWarehouse(Long id);
}
