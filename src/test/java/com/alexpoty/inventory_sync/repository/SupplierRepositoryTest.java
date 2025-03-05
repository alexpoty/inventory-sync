package com.alexpoty.inventory_sync.repository;

import com.alexpoty.inventory_sync.model.Supplier;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
class SupplierRepositoryTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private SupplierRepository supplierRepository;

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
    public void should_save_supplier() {
        // given
        Supplier supplier = createSupplier();
        // when
        Supplier actual = supplierRepository.save(supplier);
        // assert
        assertNotNull(actual);
        assertEquals(supplier.getName(), actual.getName());
        assertEquals(supplier.getContactInfo(), actual.getContactInfo());
    }

    @Test
    public void should_find_supplier() {
        // given
        Supplier supplier = createSupplier();
        supplierRepository.save(supplier);
        // when
        Supplier actual = supplierRepository.findById(1L).orElseThrow();
        // assert
        assertNotNull(actual);
        assertEquals(supplier.getName(), actual.getName());
        assertEquals(supplier.getContactInfo(), actual.getContactInfo());
    }

    @Test
    public void should_return_list_of_supplier() {
        // given
        Supplier supplier = createSupplier();
        supplierRepository.save(supplier);
        // when
        List<Supplier> actual = supplierRepository.findAll();
        // assert
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void should_update_supplier() {
        // given
        Supplier supplier = createSupplier();
        supplierRepository.save(supplier);
        supplier.setId(1L);
        supplier.setName("Updated name");
        // when
        Supplier actual = supplierRepository.save(supplier);
        // assert
        assertNotNull(actual);
        assertEquals(supplier.getName(), actual.getName());
        assertEquals(supplier.getContactInfo(), actual.getContactInfo());
    }

    @Test
    public void should_delete_supplier() {
        // given
        Supplier supplier = createSupplier();
        supplierRepository.save(supplier);
        // when
        supplierRepository.deleteById(1L);
        // assert
        assertFalse(supplierRepository.existsById(1L));
    }

    private Supplier createSupplier() {
        return Supplier.builder()
                .name("Test")
                .contactInfo("Test")
                .build();
    }
}