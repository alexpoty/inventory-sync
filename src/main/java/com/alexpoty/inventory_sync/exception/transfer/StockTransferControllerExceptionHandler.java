package com.alexpoty.inventory_sync.exception.transfer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class StockTransferControllerExceptionHandler {

    @ExceptionHandler(value = LowStockException.class)
    public ResponseEntity<Map<String, String>> handleLowStockException(LowStockException e) {
        Map<String, String> error = new HashMap<>();
        error.put("Low Stock", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
