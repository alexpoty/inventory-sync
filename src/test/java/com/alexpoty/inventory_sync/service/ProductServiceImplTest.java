package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ProductServiceImplTest {

    private final ProductServiceImpl productService;
    private final ProductRepository productRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Test")
                .description("Test")
                .price(new BigDecimal(123))
                .quantity(1)
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
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductResponse productResponse = productService.createProduct(productRequest);
        // assert
        assertNotNull(productResponse);
        assertEquals(productRequest.name(), product.getName());
        assertEquals(productRequest.description(), product.getDescription());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void should_throw_when_name_is_null() {
        // given
        ProductRequest productRequest = new ProductRequest(
                "",
                "Test",
                new BigDecimal(123),
                1,
                1L
        );
        // when
        assertThrows(MethodArgumentNotValidException.class, () -> productService.createProduct(productRequest));
    }
}