package com.alexpoty.inventory_sync.dto.supplier;

import java.io.Serializable;

public record SupplierResponse(
        Long id,
        String name,
        String contactInfo
) implements Serializable {
}
