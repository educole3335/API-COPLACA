package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.ProductCategoryDTO;
import com.coplaca.apirest.entity.ProductCategory;
import com.coplaca.apirest.repository.ProductCategoryRepository;
import com.coplaca.apirest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para gestionar categorías de productos
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    /**
     * Obtiene todas las categorías de productos
     */
    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        List<ProductCategoryDTO> categories = categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(categories);
    }

    /**
     * Obtiene una categoría específica
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva categoría
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<ProductCategoryDTO> createCategory(@RequestBody ProductCategoryDTO category) {
        ProductCategory entity = new ProductCategory();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setIcon(category.getImageUrl());
        ProductCategory saved = categoryRepository.save(entity);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    /**
     * Actualiza una categoría existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<ProductCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryDTO categoryDetails) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDetails.getName());
                    category.setDescription(categoryDetails.getDescription());
                    category.setIcon(categoryDetails.getImageUrl());
                    ProductCategory updated = categoryRepository.save(category);
                    return ResponseEntity.ok(mapToDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una categoría
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Mapea una categoría a DTO
     */
    private ProductCategoryDTO mapToDTO(ProductCategory category) {
        int productCount = (int) productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && 
                        p.getCategory().getId().equals(category.getId()))
                .count();

        return ProductCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
            .imageUrl(category.getIcon())
                .productCount(productCount)
                .build();
    }
}
