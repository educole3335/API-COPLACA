package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "AddressDTO", description = "Dirección de entrega o domicilio del usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    @Schema(description = "ID de la dirección", example = "7")
    private Long id;
    @Schema(description = "Calle", example = "Calle Principal")
    private String street;
    @Schema(description = "Número", example = "42")
    private String streetNumber;
    @Schema(description = "Piso o apartamento", example = "3B")
    private String apartment;
    @Schema(description = "Ciudad", example = "Santa Cruz de Tenerife")
    private String city;
    @Schema(description = "Código postal", example = "38003")
    private String postalCode;
    @Schema(description = "Provincia", example = "Santa Cruz de Tenerife")
    private String province;
    @Schema(description = "Latitud", example = "28.4635")
    private Double latitude;
    @Schema(description = "Longitud", example = "-16.2520")
    private Double longitude;
    @Schema(description = "Información adicional", example = "Apto 3B")
    private String additionalInfo;
    @Schema(description = "Indica si es la dirección por defecto", example = "true")
    private Boolean isDefault;
}
