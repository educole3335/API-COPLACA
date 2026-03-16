package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private AddressDTO address;
    private Long warehouseId;
    private String role; // ROLE_CUSTOMER, ROLE_DELIVERY, ROLE_LOGISTICS, ROLE_ADMIN
}
