package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.mapper.ProductMapper;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productRepository.save(productMapper.toProduct(productRequest));
        return productMapper.toProductResponse(product);
    }
}
