package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Product Controller - Create Product");
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.info("Product Controller - Get Product");
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(@RequestParam int page, @RequestParam int size) {
        log.info("Product Controller - Get Products");
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts(page, size));
    }

    @GetMapping("/warehouse")
    public ResponseEntity<Page<ProductResponse>> getProductsByWarehouseId(@RequestParam Long warehouseId,
                                                                          @RequestParam int page,
                                                                          @RequestParam int size) {
        log.info("Product Controller - Get Products By Warehouse Id");
        return ResponseEntity.status(HttpStatus.OK).body(productService
                .getProductsByWarehouseId(warehouseId, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        log.info("Product Controller - Update Product");
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Product Controller - Delete Product");
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
