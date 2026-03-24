package com.coplaca.apirest.recommendation.controller;

import com.coplaca.apirest.dto.LandingPageDTO;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.ProductRecommendationDTO;
import com.coplaca.apirest.service.RecommendationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendation-domain")
@RequiredArgsConstructor
public class RecommendationDomainController {

    private final RecommendationService recommendationService;

    @GetMapping("/landing")
    public ResponseEntity<LandingPageDTO> getLandingPage(@RequestParam(defaultValue = "anonymous") String userEmail) {
        return ResponseEntity.ok(recommendationService.generateLandingPageContent(userEmail));
    }

    @GetMapping("/landing/seasonal")
    public ResponseEntity<List<ProductDTO>> getSeasonalProducts() {
        return ResponseEntity.ok(recommendationService.getSeasonalProducts());
    }

    @GetMapping("/landing/recommendations")
    public ResponseEntity<List<ProductRecommendationDTO>> getRecommendations(
            @RequestParam(defaultValue = "anonymous") String userEmail) {
        return ResponseEntity.ok(recommendationService.getRecommendations(userEmail));
    }
}
