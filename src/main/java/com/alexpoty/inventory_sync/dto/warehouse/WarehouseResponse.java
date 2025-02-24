package com.alexpoty.inventory_sync.dto.warehouse;

import com.alexpoty.inventory_sync.model.Product;

import java.util.List;

public record WarehouseResponse(
        Long id,
        String name,
        String location,
        List<Product> products
) {}
