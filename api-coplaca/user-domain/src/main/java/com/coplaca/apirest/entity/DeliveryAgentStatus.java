package com.coplaca.apirest.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados operativos del repartidor")
public enum DeliveryAgentStatus {
    @Schema(description = "Disponible en el almacén")
    AT_WAREHOUSE,
    @Schema(description = "En reparto")
    DELIVERING,
    @Schema(description = "Fuera de servicio")
    OFFLINE
}
