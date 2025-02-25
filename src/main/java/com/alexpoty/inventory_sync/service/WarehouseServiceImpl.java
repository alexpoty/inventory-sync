package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    @Override
    public List<WarehouseResponse> getWarehouses() {
        return List.of();
    }

    @Override
    public WarehouseResponse getWarehouse(Long id) {
        return null;
    }

    @Override
    public WarehouseResponse addWarehouse(WarehouseRequest warehouseRequest) {
        return null;
    }

    @Override
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest warehouseRequest) {
        return null;
    }

    @Override
    public void deleteWarehouse(Long id) {

    }
}
