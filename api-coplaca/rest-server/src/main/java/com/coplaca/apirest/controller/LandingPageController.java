package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.LandingPageDTO;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.ProductRecommendationDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.service.RecommendationService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/landing")
@Tag(name = "02 - Landing", description = "Contenido público, recomendaciones y salud básica del front")
@RequiredArgsConstructor
public class LandingPageController {

    private final RecommendationService recommendationService;

    @GetMapping
    @Operation(summary = "Obtener landing page", description = "Genera el contenido principal de la portada según el usuario")
    public ResponseEntity<SuccessResponse<LandingPageDTO>> getLandingPage(Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        return ResponseHelper.ok(recommendationService.generateLandingPageContent(userEmail));
    }

    @GetMapping("/seasonal")
    @Operation(summary = "Productos de temporada", description = "Lista los productos más relevantes de la temporada actual")
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getSeasonalProducts() {
        return ResponseHelper.ok(recommendationService.getSeasonalProducts());
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Recomendaciones personalizadas", description = "Devuelve sugerencias de productos para el usuario actual")
    public ResponseEntity<SuccessResponse<List<ProductRecommendationDTO>>> getPersonalizedRecommendations(
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        return ResponseHelper.ok(recommendationService.getRecommendations(userEmail));
    }

    @GetMapping("/health")
    @Operation(summary = "Health de landing", description = "Verificación simple del estado del módulo de landing")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "Backend COPLACA conectado correctamente",
                "timestamp", System.currentTimeMillis()
        ));
    }
}