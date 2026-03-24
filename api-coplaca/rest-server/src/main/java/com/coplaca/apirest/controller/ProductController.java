package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.service.ProductService;
import com.coplaca.apirest.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getAllActiveProducts() {
        return ResponseHelper.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseHelper.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseHelper.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping(ApiConstants.SEARCH)
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> searchProducts(@RequestParam String query) {
        return ResponseHelper.ok(productService.searchProducts(query));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<SuccessResponse<ProductDTO>> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseHelper.created(productService.getProductById(created.getId()), "Product created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<SuccessResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {
        productService.updateProduct(id, productDetails);
        return ResponseHelper.ok(productService.getProductById(id), "Product updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseHelper.noContent();
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<SuccessResponse<ProductDTO>> adjustStock(
            @PathVariable Long id,
            @RequestParam BigDecimal delta) {
        return ResponseHelper.ok(productService.adjustStock(id, delta), "Stock adjusted successfully");
    }
}
