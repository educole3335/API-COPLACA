package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Schema(name = "LandingPageDTO", description = "Contenido principal de la página de inicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandingPageDTO {
    @ArraySchema(arraySchema = @Schema(description = "Productos destacados de temporada"))
    private List<ProductDTO> seasonalProducts;
    @ArraySchema(arraySchema = @Schema(description = "Ofertas activas del catálogo"))
    private List<SeasonalOfferDTO> activeOffers;
    @ArraySchema(arraySchema = @Schema(description = "Productos recomendados para el usuario"))
    private List<ProductRecommendationDTO> recommendedProducts;
    @Schema(description = "Número total de productos en oferta", example = "4")
    private Integer totalProductsOnSale;
    @Schema(description = "Mensaje de bienvenida", example = "¡Bienvenido a Coplaca! Descubre nuestros productos frescos")
    private String message;
}
