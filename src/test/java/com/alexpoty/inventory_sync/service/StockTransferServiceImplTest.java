package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
import com.alexpoty.inventory_sync.exception.transfer.LowStockException;
import com.alexpoty.inventory_sync.exception.warehouse.WarehouseNotFoundException;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.ProductWarehouse;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import com.alexpoty.inventory_sync.repository.ProductWarehouseRepository;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockTransferServiceImplTest {

    @Mock
    private ProductWarehouseRepository transferRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    @InjectMocks
    private StockTransferServiceImpl stockTransferService;

    private Product product;
    private Warehouse warehouse;
    private ProductWarehouse productWarehouse;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);

        warehouse = new Warehouse();
        warehouse.setId(1L);

        productWarehouse = new ProductWarehouse();
        productWarehouse.setId(1L);
        productWarehouse.setProduct(product);
        productWarehouse.setWarehouse(warehouse);
        productWarehouse.setQuantity(10);
    }

    @Test
    void addStock_ShouldAddStockSuccessfully() {
        StockTransferRequest request = new StockTransferRequest(1L, null, 1L, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(transferRepository.save(any(ProductWarehouse.class))).thenReturn(productWarehouse);

        StockTransferResponse response = stockTransferService.addStock(request);

        assertNotNull(response);
        assertEquals(1L, response.product().getId());
        assertEquals(1L, response.warehouse().getId());
        assertEquals(10, response.quantity());
    }

    @Test
    void addStock_ShouldThrowProductNotFoundException() {
        StockTransferRequest request = new StockTransferRequest(1L, null, 1L, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> stockTransferService.addStock(request));
    }

    @Test
    void addStock_ShouldThrowWarehouseNotFoundException() {
        StockTransferRequest request = new StockTransferRequest(1L, null, 1L, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WarehouseNotFoundException.class, () -> stockTransferService.addStock(request));
    }

    @Test
    void transferStock_ShouldTransferStockSuccessfully() {
        StockTransferRequest request = new StockTransferRequest(1L, 1L, 2L, 5);
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setId(2L);

        when(transferRepository.findByWarehouseIdAndProductId(1L, 1L)).thenReturn(Optional.of(productWarehouse));
        when(warehouseRepository.findById(2L)).thenReturn(Optional.of(newWarehouse));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(transferRepository.save(any(ProductWarehouse.class))).thenReturn(productWarehouse);

        List<StockTransferResponse> responseList = stockTransferService.transferStock(request);

        assertNotNull(responseList);
        assertEquals(2, responseList.size());
    }

    @Test
    void transferStock_ShouldThrowLowStockException() {
        StockTransferRequest request = new StockTransferRequest(1L, 1L, 2L, 15);
        when(transferRepository.findByWarehouseIdAndProductId(1L, 1L)).thenReturn(Optional.of(productWarehouse));

        assertThrows(LowStockException.class, () -> stockTransferService.transferStock(request));
    }
}