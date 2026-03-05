package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImage;
    private AddressDTO address;
    private Long warehouseId;
    private String warehouseName;
    private java.util.Set<String> roles;
    private boolean enabled;
}
