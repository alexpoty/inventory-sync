package com.alexpoty.inventory_sync.repository;

import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.ProductWarehouse;
import com.alexpoty.inventory_sync.model.Warehouse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
class ProductWarehouseRepositoryTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private ProductWarehouseRepository repository;

    @BeforeEach
    void setUp(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @BeforeAll
    static void start() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stop() {
        postgreSQLContainer.stop();
    }

    @Test
    public void should_save_product_warehouse() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        // when
        ProductWarehouse actual = repository.save(productWarehouse);
        // assert
        assertNotNull(actual);
        assertEquals(productWarehouse.getProduct().getName(), actual.getProduct().getName());
    }

    @Test
    public void should_findByWarehouseIdAndProductId() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        repository.save(productWarehouse);
        // when
        ProductWarehouse actual = repository.findByWarehouseIdAndProductId(1L, 1L).orElseThrow();
        // assert
        assertNotNull(actual);
        assertEquals(productWarehouse.getProduct().getName(), actual.getProduct().getName());
    }

    @Test
    public void should_findAllByWarehouseId() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        repository.save(productWarehouse);
        // when
        List<ProductWarehouse> actual =repository.findAllByWarehouseId(1L);
        // assert
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void should_findAllByProductId() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        repository.save(productWarehouse);
        // when
        List<ProductWarehouse> actual =repository.findAllByProductId(1L);
        // assert
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void should_update_product_warehouse() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        repository.save(productWarehouse);
        productWarehouse.setId(1L);
        productWarehouse.setQuantity(200);
        // when
        ProductWarehouse actual = repository.save(productWarehouse);
        // assert
        assertNotNull(actual);
        assertEquals(200, actual.getQuantity());
    }

    @Test
    public void should_find_product_warehouse_by_id() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        repository.save(productWarehouse);
        // when
        ProductWarehouse actual = repository.findById(1L).orElseThrow();
        // assert
        assertNotNull(actual);
        assertEquals(productWarehouse.getProduct().getName(), actual.getProduct().getName());
    }

    @Test
    public void should_delete_product_warehouse() {
        //given
        ProductWarehouse productWarehouse = createProductWarehouse();
        repository.save(productWarehouse);
        // when
        repository.deleteById(1L);
        // assert
        assertFalse(repository.existsById(1L));
    }

    private ProductWarehouse createProductWarehouse() {
        Warehouse warehouse = Warehouse.builder()
                .name("test")
                .location("Test")
                .build();
        Product product = Product.builder()
                .name("Test")
                .description("Test")
                .price(new BigDecimal(123))
                .created_at(Instant.now())
                .updated_at(Instant.now())
                .build();
        return ProductWarehouse.builder()
                .product(product)
                .warehouse(warehouse)
                .quantity(100)
                .build();
    }


}