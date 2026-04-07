package com.coplaca.apirest.entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Schema(name = "Warehouse", description = "Almacén operativo de la red logística")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouses")
public class Warehouse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del almacén", example = "1")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre del almacén", example = "Almacen Tenerife")
    private String name;
    
    @Column(nullable = false)
    @Schema(description = "Dirección física", example = "Poligono Industrial de Guimar, Tenerife")
    private String address;
    
    @Column(nullable = false)
    @Schema(description = "Latitud", example = "28.3128")
    private double latitude;
    
    @Column(nullable = false)
    @Schema(description = "Longitud", example = "-16.3972")
    private double longitude;
    
    @Column
    @Schema(description = "Capacidad estimada", example = "1000")
    private int capacity;
    
    @Column
    @Schema(description = "Teléfono de contacto", example = "922000001")
    private String phoneNumber;
    
    @Column
    @Schema(description = "Nombre del responsable", example = "Encargado Tenerife")
    private String managerName;
    
    @Column(nullable = false)
    @Schema(description = "Indica si el almacén está activo", example = "true")
    private boolean isActive = true;
    
    @Schema(description = "Fecha de creación", example = "2026-04-07T10:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Schema(description = "Fecha de actualización", example = "2026-04-07T10:30:00")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
