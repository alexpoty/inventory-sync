package com.alexpoty.inventory_sync.exception.supplier;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(String message) {
        super(message);
    }
}
