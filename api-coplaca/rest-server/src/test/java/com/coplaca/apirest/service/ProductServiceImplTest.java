package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.ProductCategory;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SeasonalOfferService offerService;

    @Mock
    private com.coplaca.apirest.mapper.ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProductDefaultsStockToZeroWhenNull() {
        Product product = product();
        product.setStockQuantity(null);

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product saved = productService.createProduct(product);

        assertEquals(new BigDecimal("0"), saved.getStockQuantity());
    }

    @Test
    void adjustStockNeverGoesBelowZero() {
        Product product = product();
        product.setStockQuantity(new BigDecimal("1.000"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productMapper.toDTO(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            ProductDTO dto = new ProductDTO();
            dto.setStockQuantity(p.getStockQuantity());
            return dto;
        });

        ProductDTO dto = productService.adjustStock(1L, new BigDecimal("-2.000"));

        assertEquals(new BigDecimal("0"), dto.getStockQuantity());
    }

    @Test
    void getProductByIdIncludesOfferDataWhenAvailable() {
        Product product = product();
        SeasonalOffer offer = new SeasonalOffer();
        offer.setDiscountPercentage(15.0);
        offer.setReason("Abundancia");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(offerService.getActiveOfferByProductId(1L)).thenReturn(Optional.of(offer));
        when(productMapper.toDTO(any(Product.class))).thenAnswer(invocation -> {
            return new ProductDTO();
        });

        ProductDTO dto = productService.getProductById(1L);

        assertEquals(15.0, dto.getDiscountPercentage());
        assertEquals("Abundancia", dto.getOfferReason());
    }

    @Test
    void getProductByIdThrowsWhenMissing() {
        when(productRepository.findById(44L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(44L));
    }

    private Product product() {
        ProductCategory category = new ProductCategory();
        category.setId(10L);
        category.setName("Frutas");

        Product product = new Product();
        product.setId(1L);
        product.setName("Platano");
        product.setDescription("Platano de Canarias");
        product.setUnit("kg");
        product.setUnitPrice(new BigDecimal("2.50"));
        product.setOriginalPrice(new BigDecimal("2.80"));
        product.setStockQuantity(new BigDecimal("10.000"));
        product.setCategory(category);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
}
