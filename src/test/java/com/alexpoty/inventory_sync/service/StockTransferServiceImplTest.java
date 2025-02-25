package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferRequest;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
import com.alexpoty.inventory_sync.exception.warehouse.WarehouseNotFoundException;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockTransferServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private StockTransferServiceImpl service;

    @Test
    public void should_transfer_from_warehouse_to_another() {
        // given
        StockTransferRequest stockTransferRequest = new StockTransferRequest(
                1L,
                2L,
                1L,
                1
        );
        // when
        when(warehouseRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProduct()));
        String result = service.transfer(stockTransferRequest);
        // assert
        assertEquals("Success", result);
    }

    @Test
    public void should_return_false_and_cause_when_low_stock_available() {
        // given
        StockTransferRequest stockTransferRequest = new StockTransferRequest(
                1L,
                2L,
                1L,
                20
        );
        // when
        when(warehouseRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProduct()));
        String result = service.transfer(stockTransferRequest);
        // assert
        assertEquals("Low Stock Available", result);
    }

    @Test
    public void should_throw_warehouse_not_found_exception() {
        // given
        StockTransferRequest stockTransferRequest = new StockTransferRequest(
                1L,
                2L,
                1L,
                1
        );
        // when
        when(warehouseRepository.existsById(anyLong())).thenReturn(false);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProduct()));
        // assert
        assertThrows(WarehouseNotFoundException.class, () -> service.transfer(stockTransferRequest));
    }

    @Test
    public void should_throw_product_not_found_exception() {
        // given
        StockTransferRequest stockTransferRequest = new StockTransferRequest(
                1L,
                2L,
                1L,
                1
        );
        // when
        when(warehouseRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        // assert
        assertThrows(ProductNotFoundException.class, () -> service.transfer(stockTransferRequest));
    }

    private Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .price(new BigDecimal(123))
                .quantity(1)
                .warehouse(Warehouse.builder().id(1L).build())
                .created_at(Instant.now())
                .updated_at(Instant.now())
                .build();
    }
}