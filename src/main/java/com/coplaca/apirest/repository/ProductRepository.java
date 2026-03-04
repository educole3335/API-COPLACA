package com.coplaca.apirest.repository;

import com.coplaca.apirest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrueAndStockQuantityGreaterThan(Long stock);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByIsActiveTrue();
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isActive = true")
    List<Product> searchByName(@Param("name") String name);
}
