package com.coplaca.apirest.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de un pedido")
public enum OrderStatus {
    @Schema(description = "Pendiente de confirmación")
    PENDING,
    @Schema(description = "Confirmado")
    CONFIRMED,
    @Schema(description = "Asignado a reparto")
    ASSIGNED,
    @Schema(description = "Aceptado por el repartidor")
    ACCEPTED,
    @Schema(description = "En camino")
    IN_TRANSIT,
    @Schema(description = "Entregado")
    DELIVERED,
    @Schema(description = "Cancelado")
    CANCELLED
}
