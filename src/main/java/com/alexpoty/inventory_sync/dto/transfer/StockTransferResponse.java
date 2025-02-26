package com.alexpoty.inventory_sync.dto.transfer;

import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;

public record StockTransferResponse(
        Long id,
        Product product,
        Warehouse warehouse,
        Integer quantity
) {
}
