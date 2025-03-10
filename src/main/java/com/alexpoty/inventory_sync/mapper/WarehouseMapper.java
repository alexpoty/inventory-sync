package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WarehouseMapper {

    @Mapping(target = "id", ignore = true)
    Warehouse toWarehouse(WarehouseRequest warehouseRequest);
    WarehouseResponse toWarehouseResponse(Warehouse warehouse);
}
