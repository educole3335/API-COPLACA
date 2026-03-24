package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.ETAResponseDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.service.ETAService;
import com.coplaca.apirest.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*  ETA Estimated Time of Arrival */
@RestController
@RequestMapping(ApiConstants.API_V1 + "/eta")
@RequiredArgsConstructor
public class ETAController {

    private final ETAService etaService;

    @GetMapping("/order/{orderId}/calculate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse<ETAResponseDTO>> calculateETA(@PathVariable Long orderId) {
        return ResponseHelper.ok(etaService.calculateETA(orderId));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse<ETAResponseDTO>> getLatestETA(@PathVariable Long orderId) {
        return ResponseHelper.ok(etaService.getLatestETA(orderId));
    }

    @PostMapping("/delivery-agent/{deliveryAgentId}/recalculate")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<String>> recalculateDeliveryAgentETAs(@PathVariable Long deliveryAgentId) {
        etaService.recalculateETAForDeliveryAgent(deliveryAgentId);
        return ResponseHelper.okMessage("ETAs recalculated successfully");
    }
}
