package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonalOfferDTO {
    private Long id;
    private Long productId;
    private double discountPercentage;
    private String reason;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
}
