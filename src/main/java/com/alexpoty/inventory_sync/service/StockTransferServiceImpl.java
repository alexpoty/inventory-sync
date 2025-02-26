package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import com.alexpoty.inventory_sync.repository.ProductWarehouseRepository;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTransferServiceImpl implements StockTransferService {

    private final ProductWarehouseRepository transferRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public StockTransferResponse addStock(StockTransferRequest request) {
        return null;
    }

    @Override
    public StockTransferResponse transferStock(StockTransferRequest request) {
        return null;
    }
}
