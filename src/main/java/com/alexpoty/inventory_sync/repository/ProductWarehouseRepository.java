package com.alexpoty.inventory_sync.repository;

import com.alexpoty.inventory_sync.model.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {

    Optional<ProductWarehouse> findByWarehouseIdAndProductId(long warehouseId, long productId);
    boolean existsByWarehouseIdAndProductId(long warehouseId, long productId);
    List<ProductWarehouse> findByWarehouseId(long warehouseId);
    List<ProductWarehouse> findByProductId(long productId);
}
