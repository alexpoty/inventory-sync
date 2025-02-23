package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;
    @MockitoBean
    private ProductRepository productRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
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

    @Test
    public void should_save_product() {
        // given
        ProductRequest productRequest = new ProductRequest(
                "Test",
                "Test",
                new BigDecimal(123),
                1,
                1L
        );

        // when
        when(productRepository.save(any(Product.class))).thenReturn(this.product);
        ProductResponse productResponse = productService.createProduct(productRequest);
        // assert
        assertNotNull(productResponse);
        assertEquals(productRequest.name(), product.getName());
        assertEquals(productRequest.description(), product.getDescription());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}