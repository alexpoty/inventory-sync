package com.alexpoty.inventory_sync.exception.warehouse;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
