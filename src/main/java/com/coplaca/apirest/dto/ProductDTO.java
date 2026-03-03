package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal originalPrice;
    private Long stockQuantity;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private String origin;
    private String nutritionInfo;
    private boolean isActive;
    private double discountPercentage; // From offer
    private String offerReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
