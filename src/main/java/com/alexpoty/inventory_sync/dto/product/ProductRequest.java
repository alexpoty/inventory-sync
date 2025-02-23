package com.alexpoty.inventory_sync.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @Positive
        BigDecimal price,
        Integer quantity,
        Long warehouseId
) {
}
