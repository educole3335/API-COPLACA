package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.service.SeasonalOfferService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.OFFERS)
@Tag(name = "03 - Catálogo", description = "Gestión de ofertas estacionales y promociones")
@RequiredArgsConstructor
public class SeasonalOfferController {

    private final SeasonalOfferService offerService;

    @GetMapping
    @Operation(summary = "Listar ofertas activas", description = "Devuelve las ofertas estacionales activas del catálogo")
    public ResponseEntity<SuccessResponse<List<SeasonalOfferDTO>>> getAllOffers() {
        return ResponseHelper.ok(offerService.getAllActiveOffers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener oferta por ID", description = "Recupera el detalle de una oferta concreta")
    public ResponseEntity<SuccessResponse<SeasonalOfferDTO>> getOffer(@PathVariable Long id) {
        return ResponseHelper.ok(offerService.getOfferById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Crear oferta", description = "Crea una oferta estacional para un producto")
    public ResponseEntity<SuccessResponse<SeasonalOfferDTO>> createOffer(@RequestBody SeasonalOffer offer) {
        return ResponseHelper.created(offerService.createOffer(offer), "Offer created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Actualizar oferta", description = "Modifica una oferta estacional existente")
    public ResponseEntity<SuccessResponse<SeasonalOfferDTO>> updateOffer(
            @PathVariable Long id,
            @RequestBody SeasonalOffer offerDetails) {
        return ResponseHelper.ok(offerService.updateOffer(id, offerDetails), "Offer updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Desactivar oferta", description = "Deshabilita una oferta sin eliminarla")
    public ResponseEntity<Void> deactivateOffer(@PathVariable Long id) {
        offerService.deactivateOffer(id);
        return ResponseHelper.noContent();
    }
}