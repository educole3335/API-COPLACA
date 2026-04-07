package com.coplaca.apirest.entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Address", description = "Dirección persistida de usuario o entrega")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la dirección", example = "7")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Calle", example = "Calle Principal")
    private String street;
    
    @Column
    @Schema(description = "Número", example = "42")
    private String streetNumber;
    
    @Column
    @Schema(description = "Apartamento o piso", example = "3B")
    private String apartment;
    
    @Column(nullable = false)
    @Schema(description = "Ciudad", example = "Santa Cruz de Tenerife")
    private String city;
    
    @Column(nullable = false)
    @Schema(description = "Código postal", example = "38003")
    private String postalCode;
    
    @Column(nullable = false)
    @Schema(description = "Provincia", example = "Santa Cruz de Tenerife")
    private String province;
    
    @Column(nullable = false)
    @Schema(description = "Latitud", example = "28.4635")
    private double latitude;
    
    @Column(nullable = false)
    @Schema(description = "Longitud", example = "-16.2520")
    private double longitude;
    
    @Column
    @Schema(description = "Información adicional", example = "Apto 3B")
    private String additionalInfo;
    
    @Column
    @Schema(description = "Dirección por defecto", example = "true")
    private boolean isDefault = true;
}
