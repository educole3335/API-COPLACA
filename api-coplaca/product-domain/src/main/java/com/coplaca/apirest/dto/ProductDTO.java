package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "ProductDTO", description = "Representación pública de un producto del catálogo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @Schema(description = "ID del producto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Plátano de Canarias (IGP)")
    private String name;
    @Schema(description = "Descripción larga", example = "Plátano fresco cultivado en Canarias")
    private String description;
    @Schema(description = "Unidad de venta", example = "kg")
    private String unit;
    @Schema(description = "Precio unitario actual", example = "2.50")
    private BigDecimal unitPrice;
    @Schema(description = "Precio original", example = "3.00")
    private BigDecimal originalPrice;
    @Schema(description = "Stock disponible", example = "500.000")
    private BigDecimal stockQuantity;
    @Schema(description = "URL de imagen", example = "https://example.com/banana.jpg")
    private String imageUrl;
    @Schema(description = "ID de la categoría", example = "1")
    private Long categoryId;
    @Schema(description = "Nombre de la categoría", example = "Frutas Tropicales")
    private String categoryName;
    @Schema(description = "Origen del producto", example = "Canarias")
    private String origin;
    @Schema(description = "Información nutricional", example = "Rico en potasio")
    private String nutritionInfo;
    @Schema(description = "Indica si el producto está activo", example = "true")
    private boolean isActive;
    @Schema(description = "Descuento aplicado desde oferta activa", example = "15.0")
    private double discountPercentage;
    @Schema(description = "Motivo de la oferta activa", example = "Exceso de cosecha")
    private String offerReason;
    @Schema(description = "Fecha de creación", example = "2026-04-07T10:10:00")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de actualización", example = "2026-04-07T10:15:00")
    private LocalDateTime updatedAt;
}
