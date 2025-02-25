package com.alexpoty.inventory_sync.dto.transfer;

import jakarta.validation.constraints.NotNull;

public record StockTransferRequest(
        @NotNull
        Long productId,
        @NotNull
        Long fromWarehouseId,
        @NotNull
        Long toWarehouseId,
        @NotNull
        Integer quantity
) {
}
