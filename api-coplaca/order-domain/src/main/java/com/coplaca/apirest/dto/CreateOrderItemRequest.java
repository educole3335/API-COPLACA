package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(name = "CreateOrderItemRequest", description = "Línea individual de un pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderItemRequest {
    @Schema(description = "ID del producto", example = "7")
    private Long productId;

    @Schema(description = "Cantidad solicitada", example = "2.5")
    private BigDecimal quantity;
}
