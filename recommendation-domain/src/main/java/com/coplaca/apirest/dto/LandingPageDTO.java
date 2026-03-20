package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandingPageDTO {
    private List<ProductDTO> seasonalProducts;
    private List<SeasonalOfferDTO> activeOffers;
    private List<ProductRecommendationDTO> recommendedProducts;
    private Integer totalProductsOnSale;
    private String message;
}
