package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTransferServiceImpl implements StockTransferService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final String SUCCESS = "success";
    private final String FAILURE = "failure";
    private final String LOW_STOCK = "low-stock available";

    @Override
    public String transfer(StockTransferRequest stockTransferRequest) {
        return FAILURE;
    }
}
