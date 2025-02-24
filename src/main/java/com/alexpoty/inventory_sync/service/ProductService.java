package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    Page<ProductResponse> getProducts(int page, int size);
    Page<ProductResponse> getProductsByWarehouseId(Long warehouseId, int page, int size);
    ProductResponse getProduct(Long id);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
}
