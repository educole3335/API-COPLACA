package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name = "CreateOrderRequest", description = "Solicitud para crear un pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @Schema(description = "Dirección de entrega alternativa; si es null se usa la del cliente", example = "1")
    private Long deliveryAddressId;

    @Schema(description = "Método de pago", example = "CARD")
    private String paymentMethod;

    @Schema(description = "Estado inicial del pago", example = "PENDING")
    private String paymentStatus;

    @ArraySchema(arraySchema = @Schema(description = "Líneas del pedido"))
    private List<CreateOrderItemRequest> items;
}
