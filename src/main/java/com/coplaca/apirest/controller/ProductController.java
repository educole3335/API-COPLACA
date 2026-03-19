package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        List<ProductDTO> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
    
    // ---- administrative operations ----
    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(productService.getProductById(created.getId()));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                   @RequestBody Product productDetails) {
        Product updated = productService.updateProduct(id, productDetails);
        if (updated != null) {
            return ResponseEntity.ok(productService.getProductById(id));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<ProductDTO> adjustStock(@PathVariable Long id,
                                                  @RequestParam BigDecimal delta) {
        ProductDTO dto = productService.adjustStock(id, delta);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
}
