package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.mapper.ProductMapper;
import com.coplaca.apirest.repository.ProductRepository;
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
    private final SeasonalOfferService offerService;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, SeasonalOfferService offerService, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.offerService = offerService;
        this.productMapper = productMapper;
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
    public List<Product> getAllActiveProductEntities() {
        return productRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = getProductEntityById(id);
        return convertToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            return getAllActiveProducts();
        }
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
    public Product saveProduct(Product product) {
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
        Optional<SeasonalOffer> offer = offerService.getActiveOfferByProductId(product.getId());

        ProductDTO dto = productMapper.toDTO(product);

        if (offer.isPresent()) {
            SeasonalOffer o = offer.get();
            dto.setDiscountPercentage(o.getDiscountPercentage());
            dto.setOfferReason(o.getReason());
        }

        return dto;
    }
}
