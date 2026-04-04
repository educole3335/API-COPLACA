package com.coplaca.apirest.product.controller;

import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.service.ProductService;
import com.coplaca.apirest.service.SeasonalOfferService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-domain")
@RequiredArgsConstructor
public class ProductDomainController {

    private final ProductService productService;
    private final SeasonalOfferService seasonalOfferService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(productService.getProductById(created.getId()));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updated = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(productService.getProductById(updated.getId()));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/products/{id}/stock")
    public ResponseEntity<ProductDTO> adjustStock(@PathVariable Long id, @RequestParam BigDecimal delta) {
        return ResponseEntity.ok(productService.adjustStock(id, delta));
    }

    @GetMapping("/offers")
    public ResponseEntity<List<SeasonalOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(seasonalOfferService.getAllActiveOffers());
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<SeasonalOfferDTO> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(seasonalOfferService.getOfferById(id));
    }

    @PostMapping("/offers")
    public ResponseEntity<SeasonalOfferDTO> createOffer(@RequestBody SeasonalOffer offer) {
        return ResponseEntity.ok(seasonalOfferService.createOffer(offer));
    }

    @PutMapping("/offers/{id}")
    public ResponseEntity<SeasonalOfferDTO> updateOffer(@PathVariable Long id, @RequestBody SeasonalOffer offerDetails) {
        return ResponseEntity.ok(seasonalOfferService.updateOffer(id, offerDetails));
    }

    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deactivateOffer(@PathVariable Long id) {
        seasonalOfferService.deactivateOffer(id);
        return ResponseEntity.noContent().build();
    }
}
