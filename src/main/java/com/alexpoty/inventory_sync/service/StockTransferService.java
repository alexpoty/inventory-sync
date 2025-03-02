package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;

import java.util.List;

public interface StockTransferService {
    StockTransferResponse addStock(StockTransferRequest request);
    List<StockTransferResponse> transferStock(StockTransferRequest request);
    List<StockTransferResponse> getProductsByWarehouseId(Long warehouseId);
    List<StockTransferResponse> getWarehousesByProductId(Long productId);
    StockTransferResponse getStock(Long id);
    StockTransferResponse addQuantity(Integer quantity, Long id);
}
