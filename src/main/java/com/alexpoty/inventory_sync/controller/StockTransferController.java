package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.service.StockTransferService;
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
@RequestMapping("/transfer")
@RequiredArgsConstructor
@Tag(name = "Stock Transfer API", description = "APIs for managing stock transfers between warehouses")
public class StockTransferController {

    private final StockTransferService stockTransferService;

    @Operation(summary = "Get stock by warehouse", description = "Retrieves all stock items stored in a specific warehouse.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockTransferResponse.class)))
    })
    @GetMapping("/warehouse/{id}")
    public ResponseEntity<List<StockTransferResponse>> getStockByWarehouse(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockTransferService.getProductsByWarehouseId(id));
    }

    @Operation(summary = "Get warehouses by product", description = "Retrieves all warehouses that store a specific product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouses retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockTransferResponse.class)))
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<List<StockTransferResponse>> getStocksByProduct(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockTransferService.getWarehousesByProductId(id));
    }

    @Operation(summary = "Add stock to a warehouse", description = "Adds stock of a specific product to a given warehouse.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockTransferResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product or Warehouse not found",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<StockTransferResponse> addStock(@Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockTransferService.addStock(request));
    }

    @Operation(summary = "Transfer stock between warehouses", description = "Transfers stock from one warehouse to another.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock transferred successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockTransferResponse.class))),
            @ApiResponse(responseCode = "400", description = "Insufficient stock for transfer",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Warehouse or Product not found",
                    content = @Content)
    })
    @PutMapping
    public ResponseEntity<List<StockTransferResponse>> transfer(@Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockTransferService.transferStock(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockTransferResponse> getStockById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockTransferService.getStock(id));
    }

    @PutMapping("/quantity")
    public ResponseEntity<StockTransferResponse> addQuantity(@RequestParam Integer quantity, @RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockTransferService.addQuantity(quantity, id));
    }
}
