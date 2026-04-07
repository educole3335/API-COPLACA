package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.service.ProductService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.PRODUCTS)
@Tag(name = "03 - Catálogo", description = "Consulta y gestión de productos del catálogo")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar productos activos", description = "Devuelve todos los productos activos disponibles en el catálogo")
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getAllActiveProducts() {
        return ResponseHelper.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Recupera el detalle de un producto concreto")
    public ResponseEntity<SuccessResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseHelper.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Listar productos por categoría", description = "Filtra el catálogo por la categoría indicada")
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseHelper.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping(ApiConstants.SEARCH)
    @Operation(summary = "Buscar productos", description = "Busca productos por texto en nombre y devuelve coincidencias activas")
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> searchProducts(@RequestParam String query) {
        return ResponseHelper.ok(productService.searchProducts(query));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Crear producto", description = "Alta de un producto nuevo para perfiles internos autorizados")
    public ResponseEntity<SuccessResponse<ProductDTO>> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseHelper.created(productService.getProductById(created.getId()), "Product created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos principales de un producto existente")
    public ResponseEntity<SuccessResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {
        productService.updateProduct(id, productDetails);
        return ResponseHelper.ok(productService.getProductById(id), "Product updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Desactivar producto", description = "Deshabilita un producto sin borrarlo físicamente")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseHelper.noContent();
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Ajustar stock", description = "Suma o resta stock al producto indicado")
    public ResponseEntity<SuccessResponse<ProductDTO>> adjustStock(
            @PathVariable Long id,
            @RequestParam BigDecimal delta) {
        return ResponseHelper.ok(productService.adjustStock(id, delta), "Stock adjusted successfully");
    }

    @PatchMapping("/{id}/price")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Actualizar precio", description = "Modifica el precio unitario de un producto")
    public ResponseEntity<SuccessResponse<ProductDTO>> adjustPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal value) {
        return ResponseHelper.ok(productService.adjustPrice(id, value), "Price updated successfully");
    }
}
