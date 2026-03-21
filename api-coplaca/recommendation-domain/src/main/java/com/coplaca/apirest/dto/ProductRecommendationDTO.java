package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRecommendationDTO {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private BigDecimal stockQuantity;
    private String reason; // "SEASONAL", "TRENDING", "RELATED", "ON_SALE"
    private Boolean onSale;
    private SeasonalOfferDTO offer;
}
