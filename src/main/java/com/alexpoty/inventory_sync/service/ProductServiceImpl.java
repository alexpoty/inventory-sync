package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.exception.ProductNotFoundException;
import com.alexpoty.inventory_sync.mapper.ProductMapper;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Product Service - Starting to save product");
        Product product = productRepository.save(productMapper.toProduct(productRequest));
        log.info("Product Service - Finished to save product");
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getProducts(int page, int size) {
        log.info("Product Service - Starting to get products");
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));
        Page<ProductResponse> responses = productPage.map(productMapper::toProductResponse);
        log.info("Product Service - Finished to get products");
        return responses;
    }

    @Override
    public Page<ProductResponse> getProductsByWarehouseId(Long warehouseId, int page, int size) {
        log.info("Product Service - Starting to get products by warehouse id");
        Page<Product> productPage = productRepository.findAllByWarehouseId(warehouseId, PageRequest.of(page, size));
        Page<ProductResponse> responses = productPage.map(productMapper::toProductResponse);
        log.info("Product Service - Finished to get products by warehouse id");
        return responses;
    }

    @Override
    public ProductResponse getProduct(Long id) {
        log.info("Product Service - Starting to get product by id");
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found"));
        log.info("Product Service - Finished to get product by id");
        return productMapper.toProductResponse(product);
    }
}
