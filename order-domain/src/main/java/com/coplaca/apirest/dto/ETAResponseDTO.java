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
public class ETAResponseDTO {
    private Long orderId;
    private LocalDateTime estimatedDeliveryTime;
    private Integer estimatedMinutes;
    private BigDecimal distanceKm;
    private String status;
    private Integer totalStopsOnRoute;
}
