package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.ProductDTO;
import com.coplaca.apirest.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllActiveProducts();
    List<ProductDTO> getProductsByCategory(Long categoryId);
    ProductDTO getProductById(Long id);
    List<ProductDTO> searchProducts(String name);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product productDetails);
    void disableProduct(Long id);
    ProductDTO adjustStock(Long id, BigDecimal quantityChange);
}
