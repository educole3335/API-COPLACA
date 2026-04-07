package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "SeasonalOfferDTO", description = "Oferta activa o estacional de un producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonalOfferDTO {
    @Schema(description = "ID de la oferta", example = "1")
    private Long id;
    @Schema(description = "ID del producto asociado", example = "1")
    private Long productId;
    @Schema(description = "Porcentaje de descuento", example = "15.0")
    private double discountPercentage;
    @Schema(description = "Motivo de la oferta", example = "Exceso de cosecha")
    private String reason;
    @Schema(description = "Fecha de inicio", example = "2026-04-01T00:00:00")
    private LocalDateTime startDate;
    @Schema(description = "Fecha de fin", example = "2026-04-14T23:59:59")
    private LocalDateTime endDate;
    @Schema(description = "Indica si la oferta está activa", example = "true")
    private boolean isActive;
}
