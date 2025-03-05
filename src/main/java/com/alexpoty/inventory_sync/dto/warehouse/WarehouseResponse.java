package com.alexpoty.inventory_sync.dto.warehouse;

import java.io.Serializable;

public record WarehouseResponse(
        Long id,
        String name,
        String location
) implements Serializable {
}
