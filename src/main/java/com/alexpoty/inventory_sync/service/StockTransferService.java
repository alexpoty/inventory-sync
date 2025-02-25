package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;

public interface StockTransferService {
    String transfer(StockTransferRequest stockTransferRequest);
}
