package com.alexpoty.inventory_sync.repository;

import com.alexpoty.inventory_sync.model.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {
}
