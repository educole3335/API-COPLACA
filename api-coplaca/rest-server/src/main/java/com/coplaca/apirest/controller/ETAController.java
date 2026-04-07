package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.ETAResponseDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.service.ETAService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*  ETA Estimated Time of Arrival */
@RestController
@RequestMapping(ApiConstants.API_V1 + "/eta")
@Tag(name = "04 - ETA", description = "Cálculo y recalculo del tiempo estimado de llegada")
@RequiredArgsConstructor
public class ETAController {

    private final ETAService etaService;

    @GetMapping("/order/{orderId}/calculate")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Calcular ETA", description = "Calcula el tiempo estimado de entrega del pedido")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ETA calculado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para ver este pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
        })
        public ResponseEntity<SuccessResponse<ETAResponseDTO>> calculateETA(
            @Parameter(description = "ID del pedido", example = "101") @PathVariable Long orderId) {
        return ResponseHelper.ok(etaService.calculateETA(orderId));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener último ETA", description = "Devuelve el último ETA calculado para el pedido")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ETA recuperado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para ver este pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
        })
        public ResponseEntity<SuccessResponse<ETAResponseDTO>> getLatestETA(
            @Parameter(description = "ID del pedido", example = "101") @PathVariable Long orderId) {
        return ResponseHelper.ok(etaService.getLatestETA(orderId));
    }

    @PostMapping("/delivery-agent/{deliveryAgentId}/recalculate")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    @Operation(summary = "Recalcular ETAs de repartidor", description = "Recalcula los ETA de los pedidos asignados a un repartidor")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ETAs recalculados correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos para recalcular"),
            @ApiResponse(responseCode = "404", description = "Repartidor no encontrado")
        })
        public ResponseEntity<SuccessResponse<String>> recalculateDeliveryAgentETAs(
            @Parameter(description = "ID del repartidor", example = "12") @PathVariable Long deliveryAgentId) {
        etaService.recalculateETAForDeliveryAgent(deliveryAgentId);
        return ResponseHelper.okMessage("ETAs recalculated successfully");
    }
}
