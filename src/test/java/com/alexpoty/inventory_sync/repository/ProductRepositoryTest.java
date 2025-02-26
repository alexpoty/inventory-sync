package com.alexpoty.inventory_sync.repository;

import com.alexpoty.inventory_sync.model.Product;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
class ProductRepositoryTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private ProductRepository productRepository;

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
    static void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    public void should_save_product() {
        // given
        Product product = Product.builder()
                .name("Test")
                .description("Test")
                .price(new BigDecimal(123))
                .created_at(Instant.now())
                .updated_at(Instant.now())
                .build();
        // when
        Product actual = productRepository.save(product);
        // assert
        assertNotNull(actual);
        assertEquals(product.getName(), actual.getName());
        assertEquals(product.getDescription(), actual.getDescription());
    }
}