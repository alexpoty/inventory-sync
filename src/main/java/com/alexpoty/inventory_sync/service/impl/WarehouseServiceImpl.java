package com.alexpoty.inventory_sync.service.impl;

import com.alexpoty.inventory_sync.dto.warehouse.WarehouseRequest;
import com.alexpoty.inventory_sync.dto.warehouse.WarehouseResponse;
import com.alexpoty.inventory_sync.exception.warehouse.WarehouseNotFoundException;
import com.alexpoty.inventory_sync.mapper.WarehouseMapper;
import com.alexpoty.inventory_sync.mapper.WarehouseMapperImpl;
import com.alexpoty.inventory_sync.model.Warehouse;
import com.alexpoty.inventory_sync.repository.WarehouseRepository;
import com.alexpoty.inventory_sync.service.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper = new WarehouseMapperImpl();

    @Cacheable(value = "warehouseCache", key = "'allWarehouses'")
    @Override
    public List<WarehouseResponse> getWarehouses() {
        log.info("Warehouse Service - FindAll");
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return warehouses.stream().map(warehouseMapper::toWarehouseResponse).toList();
    }

    @Cacheable(value = "warehouseCache", key = "#id")
    @Override
    public WarehouseResponse getWarehouse(Long id) {
        log.info("Warehouse Service - FindById");
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(
                () -> new WarehouseNotFoundException("Warehouse with id " + id + " not found")
        );
        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    @CacheEvict(value = "warehouseCache", key = "'allWarehouses'")
    @Override
    @Transactional
    public WarehouseResponse addWarehouse(WarehouseRequest warehouseRequest) {
        log.info("Warehouse Service - Save");
        Warehouse warehouse = warehouseRepository.save(warehouseMapper.toWarehouse(warehouseRequest));
        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    @CachePut(value = "warehouseCache", key = "#id")
    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest warehouseRequest) {
        log.info("Warehouse Service - FindById when updating");
        Warehouse old = warehouseRepository.findById(id).orElseThrow(
                () -> new WarehouseNotFoundException("Warehouse with id " + id + " not found")
        );
        Warehouse updated = warehouseMapper.toWarehouse(warehouseRequest);
        updated.setId(id);
        log.info("Warehouse Service - Update");
        warehouseRepository.save(updated);
        return warehouseMapper.toWarehouseResponse(updated);
    }

    @CacheEvict(value = "warehouseCache", key = "#id")
    @Override
    public void deleteWarehouse(Long id) {
        log.info("Warehouse Service - Find By Id when deleting");
        if (!warehouseRepository.existsById(id)) {
            throw new WarehouseNotFoundException("Warehouse with id " + id + " not found");
        }
        warehouseRepository.deleteById(id);
    }
}
