package com.alexpoty.inventory_sync.exception.warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequestMapping("/warehouses")
public class WarehouseControllerExceptionHandler {

    @ExceptionHandler(value = WarehouseNotFoundException.class)
    public ResponseEntity<Map<String, String >> handleWarehouseNotFoundException(WarehouseNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
