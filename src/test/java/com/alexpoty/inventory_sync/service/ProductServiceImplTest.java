package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.mapper.ProductMapper;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;
    private Product product;
    private ProductResponse productResponse;

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
        productResponse = new ProductResponse(
                1L,
                "Test",
                "Test",
                new BigDecimal(123),
                1,
                1L,
                Instant.now(),
                Instant.now()
        );
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
        when(productMapper.toProduct(any(ProductRequest.class))).thenReturn(product);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);
        ProductResponse productResponse = productService.createProduct(productRequest);
        // assert
        assertNotNull(productResponse);
        assertEquals(productRequest.name(), product.getName());
        assertEquals(productRequest.description(), product.getDescription());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}