package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.ProductCategoryDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.entity.ProductCategory;
import com.coplaca.apirest.repository.ProductCategoryRepository;
import com.coplaca.apirest.repository.ProductRepository;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.CATEGORIES)
@Tag(name = "03 - Catálogo", description = "Gestión de categorías de productos")
@RequiredArgsConstructor
public class CategoryController {

    private final ProductCategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Devuelve todas las categorías de productos con su contador asociado")
    public ResponseEntity<SuccessResponse<List<ProductCategoryDTO>>> getAllCategories() {
        List<ProductCategoryDTO> categories = categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseHelper.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Recupera el detalle de una categoría concreta")
    public ResponseEntity<SuccessResponse<ProductCategoryDTO>> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseHelper::ok)
                .orElseThrow(() -> new com.coplaca.apirest.exception.ResourceNotFoundException("Category not found with id: " + id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Crear categoría", description = "Alta de categorías para personal interno autorizado")
    public ResponseEntity<SuccessResponse<ProductCategoryDTO>> createCategory(@RequestBody ProductCategoryDTO category) {
        ProductCategory entity = new ProductCategory();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setIcon(category.getImageUrl());
        ProductCategory saved = categoryRepository.save(entity);
        return ResponseHelper.created(mapToDTO(saved), "Category created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Actualizar categoría", description = "Actualiza nombre, descripción e icono de una categoría")
    public ResponseEntity<SuccessResponse<ProductCategoryDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryDTO categoryDetails) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new com.coplaca.apirest.exception.ResourceNotFoundException("Category not found with id: " + id));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setIcon(categoryDetails.getImageUrl());
        ProductCategory updated = categoryRepository.save(category);

        return ResponseHelper.ok(mapToDTO(updated), "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría si ya no se utiliza")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new com.coplaca.apirest.exception.ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        return ResponseHelper.noContent();
    }

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
