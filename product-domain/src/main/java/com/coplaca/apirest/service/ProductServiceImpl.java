package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.repository.ProductRepository;
import com.coplaca.apirest.repository.SeasonalOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final SeasonalOfferRepository offerRepository;
    
    public ProductServiceImpl(ProductRepository productRepository, SeasonalOfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String query) {
        return productRepository.searchByName(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Product createProduct(Product product) {
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(BigDecimal.ZERO);
        }
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setUnitPrice(productDetails.getUnitPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImageUrl(productDetails.getImageUrl());
        product.setOrigin(productDetails.getOrigin());
        product.setNutritionInfo(productDetails.getNutritionInfo());
        product.setActive(productDetails.isActive());
        return productRepository.save(product);
    }
    
    @Override
    public void disableProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setActive(false);
        productRepository.save(product);
    }
    
    @Override
    public ProductDTO adjustStock(Long id, BigDecimal quantityChange) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        BigDecimal newQty = product.getStockQuantity().add(quantityChange);
        product.setStockQuantity(newQty.max(BigDecimal.ZERO));
        Product updated = productRepository.save(product);
        return convertToDTO(updated);
    }
    
    private ProductDTO convertToDTO(Product product) {
        Optional<SeasonalOffer> offer = offerRepository.findByProductIdAndIsActiveTrue(product.getId());
        
        ProductDTO dto = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .unit(product.getUnit())
                .unitPrice(product.getUnitPrice())
                .originalPrice(product.getOriginalPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .origin(product.getOrigin())
                .nutritionInfo(product.getNutritionInfo())
                .isActive(product.isActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
        
        if (offer.isPresent()) {
            SeasonalOffer o = offer.get();
            dto.setDiscountPercentage(o.getDiscountPercentage());
            dto.setOfferReason(o.getReason());
        }
        
        return dto;
    }
}
