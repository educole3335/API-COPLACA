package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.ETAResponseDTO;
import com.coplaca.apirest.service.ETAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar ETA (Estimated Time of Arrival) de entregas
 */
@RestController
@RequestMapping("/orders/eta")
@RequiredArgsConstructor
public class ETAController {

    private final ETAService etaService;

    /**
     * Calcula el ETA para una orden específica
     */
    @GetMapping("/{orderId}/calculate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ETAResponseDTO> calculateETA(@PathVariable Long orderId) {
        try {
            ETAResponseDTO eta = etaService.calculateETA(orderId);
            return ResponseEntity.ok(eta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene el ETA más reciente de una orden
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ETAResponseDTO> getLatestETA(@PathVariable Long orderId) {
        try {
            ETAResponseDTO eta = etaService.getLatestETA(orderId);
            return ResponseEntity.ok(eta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recalcula ETAs para todas las entregas de un repartidor
     */
    @PostMapping("/delivery-agent/{deliveryAgentId}/recalculate")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<Void> recalculateDeliveryAgentETAs(@PathVariable Long deliveryAgentId) {
        try {
            etaService.recalculateETAForDeliveryAgent(deliveryAgentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
