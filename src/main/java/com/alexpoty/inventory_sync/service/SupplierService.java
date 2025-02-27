package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest supplierRequest);
    SupplierResponse updateSupplier(Long id, SupplierRequest supplierRequest);
    List<SupplierResponse> getAllSuppliers();
    SupplierResponse getSupplier(Long id);
    void deleteSupplier(Long id);
}
