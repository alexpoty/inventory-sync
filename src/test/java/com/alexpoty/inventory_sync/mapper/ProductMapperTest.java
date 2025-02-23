package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void should_convert_product_to_dto() {
        // given
        Product product = Product.builder()
                .name("Test")
                .description("Test")
                .price(new BigDecimal(123))
                .quantity(1)
                .created_at(Instant.now())
                .updated_at(Instant.now())
                .build();
        // when
        ProductResponse actual = productMapper.toProductResponse(product);
        // assert
        assertEquals(actual.name(), product.getName());
    }

    @Test
    void should_convert_dto_to_product() {
        // given
        ProductRequest productRequest = new ProductRequest(
                "Test",
                "Test",
                new BigDecimal(123),
                1,
                1L
        );
        // when
        Product actual = productMapper.toProduct(productRequest);
        // assert
        assertNotNull(actual);
        assertEquals(productRequest.name(), actual.getName());
        assertEquals(productRequest.warehouseId(), actual.getWarehouse().getId());
    }
}