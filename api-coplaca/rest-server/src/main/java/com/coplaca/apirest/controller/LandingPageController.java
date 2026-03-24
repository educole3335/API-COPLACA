package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.LandingPageDTO;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.ProductRecommendationDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.service.RecommendationService;
import com.coplaca.apirest.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/landing")
@RequiredArgsConstructor
public class LandingPageController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<SuccessResponse<LandingPageDTO>> getLandingPage(Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        return ResponseHelper.ok(recommendationService.generateLandingPageContent(userEmail));
    }

    @GetMapping("/seasonal")
    public ResponseEntity<SuccessResponse<List<ProductDTO>>> getSeasonalProducts() {
        return ResponseHelper.ok(recommendationService.getSeasonalProducts());
    }

    @GetMapping("/recommendations")
    public ResponseEntity<SuccessResponse<List<ProductRecommendationDTO>>> getPersonalizedRecommendations(
            Authentication authentication) {
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        return ResponseHelper.ok(recommendationService.getRecommendations(userEmail));
    }
}