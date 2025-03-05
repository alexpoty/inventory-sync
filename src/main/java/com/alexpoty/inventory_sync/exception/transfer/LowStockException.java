package com.alexpoty.inventory_sync.exception.transfer;

public class LowStockException extends RuntimeException {
    public LowStockException(String message) {
        super(message);
    }
}
