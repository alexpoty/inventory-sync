package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
@Tag(name = "Supplier API", description = "Operations related to suppliers")
public class SupplierController {

    private final SupplierService service;

    @Operation(summary = "Get all suppliers", description = "Returns a list of all suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved suppliers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllSuppliers());
    }

    @Operation(summary = "Get a supplier by ID", description = "Retrieve supplier details using ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierResponse.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSupplier(id));
    }

    @Operation(summary = "Create a new supplier", description = "Creates a new supplier with given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSupplier(request));
    }

    @Operation(summary = "Update supplier details", description = "Updates supplier details for a given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SupplierResponse.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.updateSupplier(id, request));
    }

    @Operation(summary = "Delete a supplier", description = "Deletes a supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SupplierResponse> deleteSupplier(@PathVariable Long id) {
        service.deleteSupplier(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
