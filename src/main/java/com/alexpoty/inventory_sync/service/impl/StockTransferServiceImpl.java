package com.alexpoty.inventory_sync.service.impl;

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
import com.alexpoty.inventory_sync.service.StockTransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "transferCache", key = "'allTransfers'")
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

    @CacheEvict(value = "transferCache", key = "'allTransfers'")
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
        transferRepository.save(productWarehouse);
        return List.of(stockTransferResponse, stockTransferMapper.toResponse(productWarehouse));
    }

    @Cacheable(value = "transferCache", key = "'allTransfers'")
    @Override
    public List<StockTransferResponse> getProductsByWarehouseId(Long warehouseId) {
        log.info("Fetching products for warehouse {}", warehouseId);
        return transferRepository.findAllByWarehouseId(warehouseId).stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Cacheable(value = "transferCache", key = "'allTransfers'")
    @Override
    public List<StockTransferResponse> getWarehousesByProductId(Long productId) {
        log.info("Fetching warehouses for product {}", productId);
        return transferRepository.findAllByProductId(productId).stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Cacheable(value = "transferCache", key = "#id")
    @Override
    public StockTransferResponse getStock(Long id) {
        log.info("Fetching stock for id {}", id);
        ProductWarehouse productWarehouse = transferRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product warehouse dont found")
        );
        return stockTransferMapper.toResponse(productWarehouse);
    }

    @Transactional
    @CachePut(value = "transferCache", key = "#id")
    @Override
    public StockTransferResponse addQuantity(Integer quantity, Long id) {
        log.info("Adding quantity {} with id {}", quantity, id);
        ProductWarehouse productWarehouse = transferRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product warehouse dont found")
        );
        productWarehouse.setQuantity(productWarehouse.getQuantity() + quantity);
        transferRepository.save(productWarehouse);
        return stockTransferMapper.toResponse(productWarehouse);
    }
}
