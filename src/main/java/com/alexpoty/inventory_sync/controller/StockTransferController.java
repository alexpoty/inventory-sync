package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.service.StockTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class StockTransferController {

    private final StockTransferService stockTransferService;

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<List<StockTransferResponse>> getStockByWarehouse(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockTransferService.getProductsByWarehouseId(id));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List<StockTransferResponse>> getStocksByProduct(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockTransferService.getWarehousesByProductId(id));
    }

    @PostMapping
    public ResponseEntity<StockTransferResponse> addStock(@Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockTransferService.addStock(request));
    }

    @PutMapping
    public ResponseEntity<List<StockTransferResponse>> transfer(@Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockTransferService.transferStock(request));
    }
}
