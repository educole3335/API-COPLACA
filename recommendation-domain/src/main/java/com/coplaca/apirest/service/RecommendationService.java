package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.LandingPageDTO;
import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.dto.ProductRecommendationDTO;
import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.SeasonalOffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio de recomendaciones para landing page
 * Proporciona contenido personalizado y sugerencias basadas en:
 * - Productos de temporada
 * - Ofertas activas
 * - Historial de compra del usuario
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class RecommendationService {

    private final ProductService productService;
    private final SeasonalOfferService offerService;
    private final UserService userService;

    // Categorías de temporada (mes : categoría)
        private static final Map<Integer, List<Long>> SEASONAL_CATEGORIES = Map.ofEntries(
            Map.entry(1, List.of(3L, 4L, 5L)),   // Enero: Cítricos
            Map.entry(2, List.of(3L, 4L)),       // Febrero: Cítricos
            Map.entry(3, List.of(1L, 3L)),       // Marzo: Plátanos, Cítricos
            Map.entry(4, List.of(1L, 2L)),       // Abril: Plátanos, Tropicales
            Map.entry(5, List.of(1L, 2L, 5L)),   // Mayo: Plátanos, Tropicales, Verduras
            Map.entry(6, List.of(2L, 5L, 6L)),   // Junio: Tropicales, Verduras, Fresas
            Map.entry(7, List.of(2L, 5L, 6L)),   // Julio: Tropicales, Verduras, Fresas
            Map.entry(8, List.of(2L, 5L)),       // Agosto: Tropicales, Verduras
            Map.entry(9, List.of(1L, 2L, 5L)),   // Septiembre: Plátanos, Tropicales, Verduras
            Map.entry(10, List.of(1L, 3L, 5L)),  // Octubre: Plátanos, Cítricos, Verduras
            Map.entry(11, List.of(1L, 3L, 5L)),  // Noviembre: Plátanos, Cítricos, Verduras
            Map.entry(12, List.of(1L, 3L))       // Diciembre: Plátanos, Cítricos
        );

    public RecommendationService(ProductService productService,
                                 SeasonalOfferService offerService,
                                 UserService userService) {
        this.productService = productService;
        this.offerService = offerService;
        this.userService = userService;
    }

    /**
     * Genera contenido para la landing page
     */
    public LandingPageDTO generateLandingPageContent(String userEmail) {
        log.info("Generando contenido de landing page para usuario: {}", userEmail);

        // Obtener productos de temporada
        List<ProductDTO> seasonalProducts = getSeasonalProducts();

        // Obtener ofertas activas
        List<SeasonalOfferDTO> activeOffers = offerService.getAllActiveOffers();

        // Obtener recomendaciones personalizadas
        List<ProductRecommendationDTO> recommendations = getRecommendations(userEmail);

        Integer totalOnSale = (int) activeOffers.stream().count();

        return LandingPageDTO.builder()
                .seasonalProducts(seasonalProducts)
                .activeOffers(activeOffers)
                .recommendedProducts(recommendations)
                .totalProductsOnSale(totalOnSale)
                .message("¡Bienvenido a Coplaca! Descubre nuestros productos frescos")
                .build();
    }

    /**
     * Obtiene productos de la temporada actual
     */
    public List<ProductDTO> getSeasonalProducts() {
        int currentMonth = LocalDateTime.now().getMonthValue();
        List<Long> seasonalCategoryIds = SEASONAL_CATEGORIES.getOrDefault(currentMonth, List.of());

        if (seasonalCategoryIds.isEmpty()) {
            return productService.getAllActiveProducts();
        }

        return productService.getAllActiveProductEntities().stream()
                .filter(p -> p.getCategory() != null &&
                        seasonalCategoryIds.contains(p.getCategory().getId()))
                .limit(8)
                .map(p -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setDescription(p.getDescription());
                    dto.setUnitPrice(p.getUnitPrice());
                    dto.setStockQuantity(p.getStockQuantity());
                    dto.setImageUrl(p.getImageUrl());
                    return dto;
                })
                .toList();
    }

    /**
     * Obtiene recomendaciones personalizadas para un usuario
     */
    public List<ProductRecommendationDTO> getRecommendations(String userEmail) {
        List<ProductRecommendationDTO> recommendations = new ArrayList<>();

        try {
            userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
            List<Product> productsInOffers = getProductsOnSale();
            Map<Long, SeasonalOffer> offerByProduct = offerService.getCurrentActiveOffers().stream()
                .filter(offer -> offer.getProduct() != null)
                .collect(java.util.stream.Collectors.toMap(
                    offer -> offer.getProduct().getId(),
                    offer -> offer,
                    (first, second) -> first
                ));
            
            // Añadir productos en oferta
            for (Product product : productsInOffers.stream().limit(3).toList()) {
            SeasonalOffer offer = offerByProduct.get(product.getId());

                recommendations.add(ProductRecommendationDTO.builder()
                        .productId(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getUnitPrice())
                        .imageUrl(product.getImageUrl())
                        .stockQuantity(product.getStockQuantity())
                        .reason("ON_SALE")
                        .onSale(true)
                        .offer(offer != null ? mapOfferToDTO(offer) : null)
                        .build());
            }

            // Rellenar con más productos aleatorios
                    Set<Long> productsInOfferIds = productsInOffers.stream()
                        .map(Product::getId)
                        .collect(java.util.stream.Collectors.toSet());

                    List<Product> randomProducts = productService.getAllActiveProductEntities().stream()
                        .filter(p -> !productsInOfferIds.contains(p.getId()))
                    .limit(5)
                    .toList();

            for (Product product : randomProducts) {
                recommendations.add(ProductRecommendationDTO.builder()
                        .productId(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getUnitPrice())
                        .imageUrl(product.getImageUrl())
                        .stockQuantity(product.getStockQuantity())
                        .reason("TRENDING")
                        .onSale(false)
                        .build());
            }
        } catch (Exception e) {
            log.warn("Error generando recomendaciones para {}: {}", userEmail, e.getMessage());
        }

        return recommendations;
    }

    /**
     * Obtiene productos actualmente en oferta
     */
    public List<Product> getProductsOnSale() {
        return offerService.getCurrentActiveOffers().stream()
                .map(SeasonalOffer::getProduct)
            .filter(Objects::nonNull)
            .filter(Product::isActive)
            .toList();
    }

    /**
     * Mapea una oferta a DTO
     */
    private SeasonalOfferDTO mapOfferToDTO(SeasonalOffer offer) {
        SeasonalOfferDTO dto = new SeasonalOfferDTO();
        dto.setId(offer.getId());
        dto.setDiscountPercentage(offer.getDiscountPercentage());
        dto.setReason(offer.getReason());
        dto.setStartDate(offer.getStartDate());
        dto.setEndDate(offer.getEndDate());
        return dto;
    }
}
