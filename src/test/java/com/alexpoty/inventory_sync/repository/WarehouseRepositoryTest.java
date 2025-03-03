package com.alexpoty.inventory_sync.repository;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
class WarehouseRepositoryTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private WarehouseRepository warehouseRepository;

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
    public void should_save_warehouse() {
        // given
        Warehouse warehouse = createWarehouse();
        // when
        Warehouse actual = warehouseRepository.save(warehouse);
        // assert
        assertNotNull(actual);
        assertEquals(warehouse.getName(), actual.getName());
        assertEquals(warehouse.getLocation(), actual.getLocation());
    }

    @Test
    public void should_find_warehouse_by_id() {
        // given
        Warehouse warehouse = createWarehouse();
        warehouseRepository.save(warehouse);
        // when
        Warehouse actual = warehouseRepository.findById(1L).orElseThrow();
        // assert
        assertNotNull(actual);
        assertEquals(warehouse.getName(), actual.getName());
        assertEquals(warehouse.getLocation(), actual.getLocation());
    }

    @Test
    public void should_return_list_of_warehouses() {
        // given
        Warehouse warehouse = createWarehouse();
        warehouseRepository.save(warehouse);
        // when
        List<Warehouse> actual = warehouseRepository.findAll();
        // assert
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void should_update_warehouse() {
        // given
        Warehouse warehouse = createWarehouse();
        warehouseRepository.save(warehouse);
        warehouse.setId(1L);
        warehouse.setName("Updated");
        // when
        Warehouse actual = warehouseRepository.save(warehouse);
        // assert
        assertNotNull(actual);
        assertEquals(warehouse.getName(), actual.getName());
        assertEquals(warehouse.getLocation(), actual.getLocation());
    }

    @Test
    public void should_delete_warehouse() {
        // given
        Warehouse warehouse = createWarehouse();
        warehouseRepository.save(warehouse);
        // when
        warehouseRepository.deleteById(1L);
        // assert
        assertFalse(warehouseRepository.existsById(1L));
    }


    private Warehouse createWarehouse() {
        return Warehouse.builder()
                .name("test")
                .location("Test")
                .build();
    }

}