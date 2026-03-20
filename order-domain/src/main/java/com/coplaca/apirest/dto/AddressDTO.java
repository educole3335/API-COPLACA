package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    private String street;
    private String streetNumber;
    private String apartment;
    private String city;
    private String postalCode;
    private String province;
    private double latitude;
    private double longitude;
    private String additionalInfo;
    private boolean isDefault;
}
