package com.alexpoty.inventory_sync.service.impl;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Override
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {
        return null;
    }

    @Override
    public SupplierResponse updateSupplier(Long id, SupplierRequest supplierRequest) {
        return null;
    }

    @Override
    public List<SupplierResponse> getAllSuppliers() {
        return List.of();
    }

    @Override
    public SupplierResponse getSupplier(Long id) {
        return null;
    }

    @Override
    public void deleteSupplier(Long id) {

    }
}
