package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
import com.alexpoty.inventory_sync.exception.transfer.LowStockException;
import com.alexpoty.inventory_sync.exception.warehouse.WarehouseNotFoundException;
import com.alexpoty.inventory_sync.mapper.StockTransferMapper;
import com.alexpoty.inventory_sync.mapper.StockTransferMapperImpl;
import com.alexpoty.inventory_sync.model.ProductWarehouse;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import com.alexpoty.inventory_sync.repository.ProductWarehouseRepository;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTransferServiceImpl implements StockTransferService {

    private final ProductWarehouseRepository transferRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockTransferMapper stockTransferMapper = new StockTransferMapperImpl();

    @Override
    @Transactional
    public StockTransferResponse addStock(StockTransferRequest request) {
        log.info("Adding {} units of product {} to warehouse {}",
                request.quantity(), request.productId(), request.toWarehouseId());
        ProductWarehouse productWarehouse = ProductWarehouse.builder()
                .product(productRepository.findById(request.productId()).orElseThrow(
                        () -> new ProductNotFoundException("Product Not Found")
                ))
                .warehouse(warehouseRepository.findById(request.toWarehouseId()).orElseThrow(
                        () -> new WarehouseNotFoundException("Warehouse Not Found")
                ))
                .quantity(request.quantity())
                .build();
        ProductWarehouse saved = transferRepository.save(productWarehouse);
        return new StockTransferResponse(
                saved.getId(),
                saved.getProduct(),
                saved.getWarehouse(),
                saved.getQuantity()
        );
    }

    @Override
    @Transactional
    public List<StockTransferResponse> transferStock(StockTransferRequest request) {
        log.info("Transferring {} units of product {} from warehouse {} to warehouse {}",
                request.quantity(), request.productId(), request.fromWarehouseId(), request.toWarehouseId());
        ProductWarehouse productWarehouse = transferRepository
                .findByWarehouseIdAndProductId(request.fromWarehouseId(), request.productId()).orElseThrow();
        if (productWarehouse.getQuantity() < request.quantity()) {
            throw new LowStockException("Low Stock Available");
        }
        if (productWarehouse.getQuantity().equals(request.quantity())) {
            Warehouse warehouse = warehouseRepository.findById(request.toWarehouseId()).orElseThrow(
                    () -> new WarehouseNotFoundException("Warehouse Not Found")
            );
            productWarehouse.setWarehouse(warehouse);
            transferRepository.save(productWarehouse);
            return List.of(stockTransferMapper.toResponse(productWarehouse));
        }
        StockTransferResponse stockTransferResponse = addStock(request);
        productWarehouse.setQuantity(productWarehouse.getQuantity() - request.quantity());
        log.info("StockTransferService::transferStock - transferRepository::save");
        transferRepository.save(productWarehouse);
        return List.of(stockTransferResponse, stockTransferMapper.toResponse(productWarehouse));
    }

    @Override
    public List<StockTransferResponse> getProductsByWarehouseId(Long warehouseId) {
        log.info("Fetching products for warehouse {}", warehouseId);
        return transferRepository.findAllByWarehouseId(warehouseId).stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockTransferResponse> getWarehousesByProductId(Long productId) {
        log.info("Fetching warehouses for product {}", productId);
        return transferRepository.findAllByProductId(productId).stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }
}
