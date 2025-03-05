package com.alexpoty.inventory_sync.dto.supplier;

import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(
        @NotBlank
        String name,
        @NotBlank
        String contactInfo
) {
}
