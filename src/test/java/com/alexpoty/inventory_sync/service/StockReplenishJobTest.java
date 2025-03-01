package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.ProductWarehouse;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductWarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockReplenishJobTest {

    @Mock
    private ProductWarehouseRepository productWarehouseRepository;

    @Mock
    private MailSenderService mailSenderService;

    @InjectMocks
    private StockReplenishJob stockReplenishJob;

    private List<ProductWarehouse> productWarehouses;

    @BeforeEach
    void setUp() {
        Product product1 = new Product();
        product1.setName("Laptop");

        Product product2 = new Product();
        product2.setName("Phone");

        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("Warehouse A");

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setName("Warehouse B");

        productWarehouses = List.of(
                new ProductWarehouse(1L, product1, warehouse1, 5),  // Low stock
                new ProductWarehouse(2L, product2, warehouse2, 20) // Sufficient stock
        );
    }

    @Test
    void checkStockForLowQuantity_ShouldSendEmail_WhenLowStockFound() {
        // Given
        when(productWarehouseRepository.findAll()).thenReturn(productWarehouses);

        // When
        stockReplenishJob.checkStockForLowQuantity();

        // Then
        verify(productWarehouseRepository, times(1)).findAll();
        verify(mailSenderService, times(1)).sendMail(Map.of("Laptop", "Warehouse A"));
    }

    @Test
    void checkStockForLowQuantity_ShouldNotSendEmail_WhenNoLowStock() {
        // Given
        ProductWarehouse product = new ProductWarehouse(1L, new Product(), new Warehouse(), 50); // High stock
        when(productWarehouseRepository.findAll()).thenReturn(List.of(product));

        // When
        stockReplenishJob.checkStockForLowQuantity();

        // Then
        verify(productWarehouseRepository, times(1)).findAll();
        verify(mailSenderService, never()).sendMail(any());
    }
}
