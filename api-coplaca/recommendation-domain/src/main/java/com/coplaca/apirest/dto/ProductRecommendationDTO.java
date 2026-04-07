package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Schema(name = "ProductRecommendationDTO", description = "Elemento recomendado en la landing page")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRecommendationDTO {
    @Schema(description = "ID del producto", example = "1")
    private Long productId;
    @Schema(description = "Nombre del producto", example = "Mango")
    private String name;
    @Schema(description = "Descripción del producto", example = "Mango fresco y tropical")
    private String description;
    @Schema(description = "Precio actual", example = "3.80")
    private BigDecimal price;
    @Schema(description = "URL de imagen", example = "https://example.com/mango.jpg")
    private String imageUrl;
    @Schema(description = "Stock disponible", example = "200.000")
    private BigDecimal stockQuantity;
    @Schema(description = "Motivo de la recomendación", example = "ON_SALE", allowableValues = {"SEASONAL", "TRENDING", "RELATED", "ON_SALE"})
    private String reason;
    @Schema(description = "Indica si está en oferta", example = "true")
    private Boolean onSale;
    @Schema(description = "Oferta asociada, si existe")
    private SeasonalOfferDTO offer;
}
