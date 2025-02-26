package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;

public interface StockTransferService {
    StockTransferResponse addStock(StockTransferRequest request);
    StockTransferResponse transferStock(StockTransferRequest request);
}
