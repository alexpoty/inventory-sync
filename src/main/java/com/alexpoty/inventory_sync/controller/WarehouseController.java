package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
@Slf4j
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    ResponseEntity<List<WarehouseResponse>> getWarehouses() {
        log.info("Warehouse Controller - getWarehouses");
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.getWarehouses());
    }

    @GetMapping("/{id}")
    ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
        log.info("Warehouse Controller - getWarehouseById");
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.getWarehouse(id));
    }

    @PostMapping
    ResponseEntity<WarehouseResponse> addWarehouse(@Valid @RequestBody WarehouseRequest warehouseRequest) {
        log.info("Warehouse Controller - addWarehouse");
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.addWarehouse(warehouseRequest));
    }

    @PutMapping("/{id}")
    ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id, @RequestBody WarehouseRequest warehouseRequest) {
        log.info("Warehouse Controller - updateWarehouse");
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.updateWarehouse(id, warehouseRequest));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        log.info("Warehouse Controller - deleteWarehouse");
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
