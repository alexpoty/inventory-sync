package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
import com.alexpoty.inventory_sync.mapper.ProductMapper;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void should_find_products() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);
        // when
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);
        Page<ProductResponse> actual = productService.getProducts(0, 10);
        // assert
        assertEquals(productPage.getTotalElements(), actual.getTotalElements());
        assertEquals(productPage.getTotalPages(), actual.getTotalPages());
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void should_find_products_by_warehouse() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);
        // when
        when(productRepository.findAllByWarehouseId(any(Long.class), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);
        Page<ProductResponse> actual = productService.getProductsByWarehouseId(1L, 0, 10);
        // assert
        assertEquals(productPage.getTotalPages(), actual.getTotalPages());
        assertEquals(productPage.getTotalElements(), actual.getTotalElements());
        ProductResponse actualProduct = actual.getContent().getFirst();
        assertEquals(actualProduct.name(), product.getName());
        assertEquals(actualProduct.description(), product.getDescription());
        verify(productRepository, times(1)).findAllByWarehouseId(any(Long.class), any(Pageable.class));
    }

    @Test
    public void should_find_product_by_id() {
        // when
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);
        ProductResponse actual = productService.getProduct(1L);
        // assert
        assertEquals(actual.name(), product.getName());
        assertEquals(actual.description(), product.getDescription());
    }

    @Test
    public void should_throw_when_no_product_found() {
        // when
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // assertThrow
        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(1L));
    }

    @Test
    public void should_update_product() {
        ProductRequest productRequest = new ProductRequest(
                "Test",
                "Test",
                new BigDecimal(123),
                1,
                1L
        );
        Product given  = Product.builder()
                .name("Test")
                .description("Test")
                .price(new BigDecimal(123))
                .quantity(1)
                .warehouse(Warehouse.builder().id(1L).build())
                .created_at(Instant.now())
                .updated_at(Instant.now())
                .build();
        // when
        when(productRepository.existsById(any(Long.class))).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toProduct(any(ProductRequest.class))).thenReturn(given);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(given));
        when(productMapper.toProductResponse(any(Product.class))).thenReturn(productResponse);
        ProductResponse actual = productService.updateProduct(1L, productRequest);
        // assert
        assertEquals(actual.name(), product.getName());
        assertEquals(actual.id(), 1L);
        assertEquals(actual.description(), product.getDescription());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productRepository, times(1)).existsById(any(Long.class));
    }

    @Test
    public void should_throw_when_updated_product_not_found() {
        // given
        ProductRequest productRequest = new ProductRequest(
                "Test",
                "Test",
                new BigDecimal(123),
                1,
                1L
        );
        // when
        when(productRepository.existsById(any(Long.class))).thenReturn(false);
        // assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productRequest));
    }

    @Test
    public void should_delete_product() {
        // when
        Mockito.doNothing().when(productRepository).deleteById(any(Long.class));
        when(productRepository.existsById(any(Long.class))).thenReturn(true);
        productService.deleteProduct(1L);
        // assert
        verify(productRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void should_throw_when_delete_product_not_found() {
        // when
        when(productRepository.existsById(any(Long.class))).thenReturn(false);
        // assert
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}