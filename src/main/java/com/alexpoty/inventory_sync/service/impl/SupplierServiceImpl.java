package com.alexpoty.inventory_sync.service.impl;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.exception.supplier.SupplierNotFoundException;
import com.alexpoty.inventory_sync.mapper.SupplierMapper;
import com.alexpoty.inventory_sync.mapper.SupplierMapperImpl;
import com.alexpoty.inventory_sync.model.Supplier;
import com.alexpoty.inventory_sync.repository.SupplierRepository;
import com.alexpoty.inventory_sync.service.SupplierService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper = new SupplierMapperImpl();

    @CacheEvict(value = "supplierCache", key = "'allSuppliers'")
    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {
        log.info("Creating new supplier: {}", supplierRequest);
        Supplier supplier = supplierRepository.save(supplierMapper.toSupplier(supplierRequest));
        return supplierMapper.toSupplierResponse(supplier);
    }

    @CachePut(value = "supplierCache", key = "#id")
    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest supplierRequest) {
        log.info("Updating supplier: with id: {}, and request: {}", id, supplierRequest);
        if (!supplierRepository.existsById(id)) {
            throw new SupplierNotFoundException("Supplier with id " + id + " not found");
        }
        Supplier supplier = supplierMapper.toSupplier(supplierRequest);
        supplier.setId(id);
        supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Cacheable(value = "supplierCache", key = "'allSuppliers'")
    @Override
    public List<SupplierResponse> getAllSuppliers() {
        log.info("Getting all suppliers");
        return supplierRepository.findAll().stream().map(supplierMapper::toSupplierResponse).toList();
    }

    @Cacheable(value = "supplierCache", key = "#id")
    @Override
    public SupplierResponse getSupplier(Long id) {
        log.info("Getting supplier: with id: {}", id);
        Supplier supplier = supplierRepository.findById(id).orElseThrow(
                () -> new SupplierNotFoundException("Supplier with id " + id + " not found")
        );
        return supplierMapper.toSupplierResponse(supplier);
    }

    @CacheEvict(value = "supplierCache", key = "#id")
    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier: with id: {}", id);
        if (!supplierRepository.existsById(id)) {
            throw new SupplierNotFoundException("Supplier with id " + id + " not found");
        }
        supplierRepository.deleteById(id);
    }
}
