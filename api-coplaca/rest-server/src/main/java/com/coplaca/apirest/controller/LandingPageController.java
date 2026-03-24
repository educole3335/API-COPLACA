package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.LandingPageDTO;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.ProductRecommendationDTO;
import com.coplaca.apirest.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para landing page y recomendaciones
 */
@RestController
@RequestMapping("/landing")
@RequiredArgsConstructor
public class LandingPageController {

    private final RecommendationService recommendationService;

    /**
     * Obtiene contenido completo de la landing page
     */
    @GetMapping
    public ResponseEntity<LandingPageDTO> getLandingPage(Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        LandingPageDTO content = recommendationService.generateLandingPageContent(userEmail);
        return ResponseEntity.ok(content);
    }

    /**
     * Obtiene productos de temporada
     */
    @GetMapping("/seasonal")
    public ResponseEntity<List<ProductDTO>> getSeasonalProducts() {
        List<ProductDTO> products = recommendationService.getSeasonalProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Obtiene recomendaciones personalizadas para el usuario
     */
    @GetMapping("/recommendations")
    public ResponseEntity<List<ProductRecommendationDTO>> getPersonalizedRecommendations(
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        List<ProductRecommendationDTO> recommendations = 
            recommendationService.getRecommendations(userEmail);
        return ResponseEntity.ok(recommendations);
    }
}
