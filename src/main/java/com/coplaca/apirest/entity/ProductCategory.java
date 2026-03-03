package com.coplaca.apirest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_categories")
public class ProductCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column
    private String description;
    
    @Column
    private String icon;
    
    @Column(columnDefinition = "TEXT")
    private String color;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
