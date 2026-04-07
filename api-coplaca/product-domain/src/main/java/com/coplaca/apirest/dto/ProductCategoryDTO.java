package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "ProductCategoryDTO", description = "Categoría del catálogo de productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryDTO {
    @Schema(description = "ID de la categoría", example = "1")
    private Long id;
    @Schema(description = "Nombre de la categoría", example = "Frutas Tropicales")
    private String name;
    @Schema(description = "Descripción de la categoría", example = "Frutas tropicales frescas de Coplaca")
    private String description;
    @Schema(description = "Icono o imagen de la categoría", example = "🍌")
    private String imageUrl;
    @Schema(description = "Número de productos dentro de la categoría", example = "4")
    private Integer productCount;
}
