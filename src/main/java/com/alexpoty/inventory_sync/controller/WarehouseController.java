package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Warehouse API", description = "Operations related to warehouse management")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Operation(summary = "Get all warehouses", description = "Retrieves a list of all warehouses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    ResponseEntity<List<WarehouseResponse>> getWarehouses() {
        log.info("Warehouse Controller - getWarehouses");
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.getWarehouses());
    }

    @Operation(summary = "Get a warehouse by ID", description = "Retrieves a warehouse by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved warehouse"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
        log.info("Warehouse Controller - getWarehouseById");
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.getWarehouse(id));
    }

    @Operation(summary = "Add a new warehouse", description = "Creates a new warehouse record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Warehouse created successfully")
    })
    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    ResponseEntity<WarehouseResponse> addWarehouse(@Valid @RequestBody WarehouseRequest warehouseRequest) {
        log.info("Warehouse Controller - addWarehouse");
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.addWarehouse(warehouseRequest));
    }

    @Operation(summary = "Update an existing warehouse", description = "Updates the details of a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouse updated successfully"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id, @RequestBody WarehouseRequest warehouseRequest) {
        log.info("Warehouse Controller - updateWarehouse");
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.updateWarehouse(id, warehouseRequest));
    }

    @Operation(summary = "Delete a warehouse", description = "Deletes a warehouse by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Warehouse deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        log.info("Warehouse Controller - deleteWarehouse");
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
