package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    Supplier toSupplier(SupplierRequest request);
    SupplierResponse toSupplierResponse(Supplier supplier);
}
