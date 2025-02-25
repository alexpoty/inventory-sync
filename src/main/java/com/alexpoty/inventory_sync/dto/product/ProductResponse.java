package com.alexpoty.inventory_sync.dto.product;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Long warehouseId,
        Instant created_at,
        Instant updated_at
) {}
