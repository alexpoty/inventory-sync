package com.alexpoty.inventory_sync.controller;

import com.alexpoty.inventory_sync.dto.product.ProductRequest;
import com.alexpoty.inventory_sync.dto.product.ProductResponse;
import com.alexpoty.inventory_sync.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product API", description = "Operations related to products management")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product and returns its details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Product Controller - Create Product");
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productRequest));
    }

    @Operation(summary = "Get product by ID", description = "Retrieves product details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.info("Product Controller - Get Product");
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(id));
    }

    @Operation(summary = "Get paginated products", description = "Retrieves a paginated list of products.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(@RequestParam int page, @RequestParam int size) {
        log.info("Product Controller - Get Products");
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts(page, size));
    }

    @Operation(summary = "Update an existing product", description = "Updates product details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        log.info("Product Controller - Update Product");
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id, productRequest));
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Product Controller - Delete Product");
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
