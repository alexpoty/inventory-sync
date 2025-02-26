package com.alexpoty.inventory_sync.dto.warehouse;

import jakarta.validation.constraints.NotBlank;

public record WarehouseRequest(
        @NotBlank
        String name,
        String location
) {
}
