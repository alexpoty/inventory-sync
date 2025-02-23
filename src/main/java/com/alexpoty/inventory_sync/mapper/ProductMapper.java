package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "warehouseId", source = "warehouse")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "warehouse", source = "warehouseId")
    @Mapping(target = "updated_at", expression = "java(java.time.Instant.now())")
    @Mapping(target = "created_at", expression = "java(java.time.Instant.now())")
    Product toProduct(ProductRequest productRequest);

    default Long map(Warehouse warehouse) {
        return warehouse != null ? warehouse.getId() : null;
    }

    default Warehouse map(Long warehouseId) {
        return Warehouse.builder()
                .id(warehouseId)
                .build();
    }
}
