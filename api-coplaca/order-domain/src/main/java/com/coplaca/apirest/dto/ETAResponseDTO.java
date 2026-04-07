package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "ETAResponseDTO", description = "Resultado del cálculo de tiempo estimado de llegada")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ETAResponseDTO {
    @Schema(description = "ID del pedido", example = "101")
    private Long orderId;
    @Schema(description = "Hora estimada de entrega", example = "2026-04-07T12:45:00")
    private LocalDateTime estimatedDeliveryTime;
    @Schema(description = "Minutos estimados restantes", example = "35")
    private Integer estimatedMinutes;
    @Schema(description = "Distancia en kilómetros", example = "4.75")
    private BigDecimal distanceKm;
    @Schema(description = "Estado del pedido al calcular ETA", example = "IN_TRANSIT")
    private String status;
    @Schema(description = "Número total de paradas en ruta", example = "2")
    private Integer totalStopsOnRoute;
}
