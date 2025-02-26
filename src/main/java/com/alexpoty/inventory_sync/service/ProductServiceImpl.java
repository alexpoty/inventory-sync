package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.exception.product.ProductNotFoundException;
import com.alexpoty.inventory_sync.mapper.ProductMapper;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
    public ProductResponse getProduct(Long id) {
        log.info("Product Service - Starting to get product by id");
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product  with id: " + id + " not found"));
        log.info("Product Service - Finished to get product by id");
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        log.info("Product Service - Searching by id when updating product");
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        Product product = productMapper.toProduct(productRequest);
        product.setId(id);
        product.setCreated_at(productRepository.findById(id).orElseThrow().getCreated_at());
        product.setUpdated_at(Instant.now());
        log.info("Product Service - Saving Updated Product");
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Product Service - Checking if product exist when deleting product");
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}
