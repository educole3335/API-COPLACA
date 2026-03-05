package com.coplaca.apirest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String street;
    
    @Column
    private String streetNumber;
    
    @Column
    private String apartment;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String postalCode;
    
    @Column(nullable = false)
    private String province;
    
    @Column(nullable = false)
    private double latitude;
    
    @Column(nullable = false)
    private double longitude;
    
    @Column
    private String additionalInfo;
    
    @Column
    private boolean isDefault = true;
}
